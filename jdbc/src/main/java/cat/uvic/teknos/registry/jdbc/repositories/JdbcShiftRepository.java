package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.exceptions.CrudException;
import cat.uvic.teknos.registry.jdbc.exceptions.TransactionException;
import cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Shift;
import cat.uvic.teknos.registry.repositories.ShiftRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcShiftRepository implements ShiftRepository {

    private final DataSource dataSource;
    private final ModelFactory modelFactory = new JdbcModelFactory();

    // SQL Statements
    private static final String INSERT_SHIFT = "INSERT INTO SHIFT (name, start_time, end_time, location) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_SHIFT = "UPDATE SHIFT SET name = ?, start_time = ?, end_time = ?, location = ? WHERE shift_id = ?";
    private static final String DELETE_EMPLOYEE_SHIFTS_BY_SHIFT = "DELETE FROM EMPLOYEE_SHIFT WHERE shift_id = ?";
    private static final String DELETE_SHIFT = "DELETE FROM SHIFT WHERE shift_id = ?";
    private static final String GET_SHIFT_BY_ID = "SELECT * FROM SHIFT WHERE shift_id = ?";
    private static final String GET_ALL_SHIFTS = "SELECT * FROM SHIFT";
    private static final String GET_SHIFTS_BY_LOCATION = "SELECT * FROM SHIFT WHERE location = ?";
    private static final String GET_EMPLOYEES_FOR_SHIFT = "SELECT e.employee_id, e.first_name, e.last_name FROM EMPLOYEE e JOIN EMPLOYEE_SHIFT es ON e.employee_id = es.employee_id WHERE es.shift_id = ?";

    public JdbcShiftRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Shift shift) {
        if (shift == null) return;

        try (Connection connection = dataSource.getConnection()) {
            if (shift.getId() <= 0) {
                insert(connection, shift);
            } else {
                update(connection, shift);
            }
        } catch (SQLException e) {
            throw new CrudException("Error saving shift.", e);
        }
    }

    private void insert(Connection connection, Shift shift) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SHIFT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, shift.getName());
            statement.setTime(2, shift.getStartTime());
            statement.setTime(3, shift.getEndTime());
            statement.setString(4, shift.getLocation());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    shift.setId(keys.getInt(1));
                } else {
                    throw new CrudException("Failed to retrieve generated key for new shift.");
                }
            }
        }
    }

    private void update(Connection connection, Shift shift) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_SHIFT)) {
            statement.setString(1, shift.getName());
            statement.setTime(2, shift.getStartTime());
            statement.setTime(3, shift.getEndTime());
            statement.setString(4, shift.getLocation());
            statement.setInt(5, shift.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Could not update shift with ID " + shift.getId() + ". It may not exist.");
            }
        }
    }

    @Override
    public void delete(Shift shift) {
        if (shift == null || shift.getId() <= 0) return;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement assocStatement = connection.prepareStatement(DELETE_EMPLOYEE_SHIFTS_BY_SHIFT);
                 PreparedStatement shiftStatement = connection.prepareStatement(DELETE_SHIFT)) {

                assocStatement.setInt(1, shift.getId());
                assocStatement.executeUpdate();

                shiftStatement.setInt(1, shift.getId());
                shiftStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new TransactionException("Error during delete transaction for shift, rollback initiated.", e);
            }
        } catch (SQLException e) {
            throw new CrudException("Failed to get connection for shift delete.", e);
        }
    }

    @Override
    public Shift get(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SHIFT_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAndFetchEager(connection, resultSet);
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving shift with ID: " + id, e);
        }
        return null;
    }

    @Override
    public Set<Shift> getAll() {
        Set<Shift> shifts = new HashSet<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_SHIFTS)) {
            while (resultSet.next()) {
                shifts.add(mapAndFetchEager(connection, resultSet));
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving all shifts.", e);
        }
        return shifts;
    }

    @Override
    public Set<Shift> getByLocation(String location) {
        Set<Shift> shifts = new HashSet<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_SHIFTS_BY_LOCATION)) {
            statement.setString(1, location);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    shifts.add(mapAndFetchEager(connection, resultSet));
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving shifts by location: " + location, e);
        }
        return shifts;
    }

    private Shift mapAndFetchEager(Connection connection, ResultSet resultSet) throws SQLException {
        Shift shift = modelFactory.newShift();
        shift.setId(resultSet.getInt("shift_id"));
        shift.setName(resultSet.getString("name"));
        shift.setStartTime(resultSet.getTime("start_time"));
        shift.setEndTime(resultSet.getTime("end_time"));
        shift.setLocation(resultSet.getString("location"));

        shift.setEmployees(loadEmployeesForShift(connection, shift.getId()));

        return shift;
    }

    private Set<Employee> loadEmployeesForShift(Connection connection, int shiftId) throws SQLException {
        Set<Employee> employees = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_EMPLOYEES_FOR_SHIFT)) {
            statement.setInt(1, shiftId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Employee employee = modelFactory.newEmployee();
                    employee.setId(rs.getInt("employee_id"));
                    employee.setFirstName(rs.getString("first_name"));
                    employee.setLastName(rs.getString("last_name"));
                    employees.add(employee);
                }
            }
        }
        return employees;
    }
}