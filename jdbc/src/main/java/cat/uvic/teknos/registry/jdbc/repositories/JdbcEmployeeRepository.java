package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.exceptions.CrudException;
import cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory;
import cat.uvic.teknos.registry.models.*;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcEmployeeRepository implements EmployeeRepository {

    private final DataSource dataSource;
    private final ModelFactory modelFactory = new JdbcModelFactory();

    // SQL Statements
    private static final String INSERT_EMPLOYEE = "INSERT INTO EMPLOYEE (first_name, last_name, email, phone_number, hire_date) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_EMPLOYEE = "UPDATE EMPLOYEE SET first_name = ?, last_name = ?, email = ?, phone_number = ?, hire_date = ? WHERE employee_id = ?";
    private static final String DELETE_EMPLOYEE_SHIFTS = "DELETE FROM EMPLOYEE_SHIFT WHERE employee_id = ?";
    private static final String INSERT_EMPLOYEE_SHIFT = "INSERT INTO EMPLOYEE_SHIFT (employee_id, shift_id) VALUES (?, ?)";
    private static final String DELETE_ALL_DEPENDENCIES = "DELETE FROM EMPLOYEE_DETAIL WHERE employee_id = ?; DELETE FROM TIME_LOG WHERE employee_id = ?; DELETE FROM EMPLOYEE_SHIFT WHERE employee_id = ?; DELETE FROM EMPLOYEE_TRAINING WHERE employee_id = ?;";
    private static final String DELETE_EMPLOYEE = "DELETE FROM EMPLOYEE WHERE employee_id = ?";
    private static final String GET_EMPLOYEE_BY_ID = "SELECT * FROM EMPLOYEE WHERE employee_id = ?";
    private static final String GET_EMPLOYEE_BY_EMAIL = "SELECT * FROM EMPLOYEE WHERE email = ?";
    private static final String GET_ALL_EMPLOYEES = "SELECT * FROM EMPLOYEE";
    private static final String GET_SHIFTS_FOR_EMPLOYEE = "SELECT s.* FROM SHIFT s JOIN EMPLOYEE_SHIFT es ON s.shift_id = es.shift_id WHERE es.employee_id = ?";
    private static final String GET_TRAININGS_FOR_EMPLOYEE = "SELECT et.*, t.title FROM EMPLOYEE_TRAINING et JOIN TRAINING t ON et.training_id = t.training_id WHERE et.employee_id = ?";


    public JdbcEmployeeRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Employee employee) {
        if (employee == null) return;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try {
                if (employee.getId() <= 0) {
                    insert(connection, employee);
                } else {
                    update(connection, employee);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error during save transaction, rollback initiated.", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get connection for save.", e);
        }
    }

    private void insert(Connection connection, Employee employee) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getEmail());
            statement.setString(4, employee.getPhoneNumber());
            statement.setDate(5, employee.getHireDate());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    employee.setId(keys.getInt(1));
                }
            }
            updateShifts(connection, employee);
        } catch (SQLException e) {
            throw new CrudException("Failed to insert employee with id " + employee.getId());
        }
    }

    private void update(Connection connection, Employee employee) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_EMPLOYEE)) {
            statement.setString(1, employee.getFirstName());
            statement.setString(2, employee.getLastName());
            statement.setString(3, employee.getEmail());
            statement.setString(4, employee.getPhoneNumber());
            statement.setDate(5, employee.getHireDate());
            statement.setInt(6, employee.getId());
            statement.executeUpdate();
            updateShifts(connection, employee);
        }
    }

    private void updateShifts(Connection connection, Employee employee) throws SQLException {
        // First, remove all existing shift associations for this employee
        try (PreparedStatement statement = connection.prepareStatement(DELETE_EMPLOYEE_SHIFTS)) {
            statement.setInt(1, employee.getId());
            statement.executeUpdate();
        }

        // Second, add the new shift associations from the employee object
        if (employee.getShifts() == null || employee.getShifts().isEmpty()) return;
        try (PreparedStatement statement = connection.prepareStatement(INSERT_EMPLOYEE_SHIFT)) {
            for (Shift shift : employee.getShifts()) {
                statement.setInt(1, employee.getId());
                statement.setInt(2, shift.getId());
                statement.addBatch();
            }
            statement.executeBatch();
        }
    }

    @Override
    public void delete(Employee employee) {
        if (employee == null || employee.getId() <= 0) return;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement depStatement = connection.prepareStatement(DELETE_ALL_DEPENDENCIES);
                 PreparedStatement empStatement = connection.prepareStatement(DELETE_EMPLOYEE)) {

                // Delete all dependencies first
                depStatement.setInt(1, employee.getId());
                depStatement.setInt(2, employee.getId());
                depStatement.setInt(3, employee.getId());
                depStatement.setInt(4, employee.getId());
                depStatement.execute();

                // Delete the employee itself
                empStatement.setInt(1, employee.getId());
                empStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Error during delete transaction, rollback initiated.", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get connection for delete.", e);
        }
    }

    @Override
    public Employee get(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_EMPLOYEE_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAndFetchEager(connection, resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving employee by ID.", e);
        }
        return null;
    }

    @Override
    public Set<Employee> getAll() {
        Set<Employee> employees = new HashSet<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_EMPLOYEES)) {
            while (resultSet.next()) {
                employees.add(mapAndFetchEager(connection, resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving all employees.", e);
        }
        return employees;
    }

    @Override
    public Employee getByEmail(String email) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_EMPLOYEE_BY_EMAIL)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAndFetchEager(connection, resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving employee by email.", e);
        }
        return null;
    }

    @Override
    public Set<EmployeeTraining> getTrainingDetailsForEmployee(Employee employee) {
        if (employee == null || employee.getId() <= 0) return new HashSet<>();
        try (Connection connection = dataSource.getConnection()) {
            return loadTrainings(connection, employee);
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving training details for employee.", e);
        }
    }

    private Employee mapAndFetchEager(Connection connection, ResultSet resultSet) throws SQLException {
        // 1. Map the primary Employee object
        Employee employee = modelFactory.newEmployee();
        employee.setId(resultSet.getInt("employee_id"));
        employee.setFirstName(resultSet.getString("first_name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setEmail(resultSet.getString("email"));
        employee.setPhoneNumber(resultSet.getString("phone_number"));
        employee.setHireDate(resultSet.getDate("hire_date"));

        // 2. Eagerly fetch and set all related collections
        employee.setShifts(loadShifts(connection, employee));
        employee.setEmployeeTrainings(loadTrainings(connection, employee));
        // Note: Loading EmployeeDetail and TimeLog would follow the same pattern

        return employee;
    }

    private Set<Shift> loadShifts(Connection connection, Employee employee) throws SQLException {
        Set<Shift> shifts = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_SHIFTS_FOR_EMPLOYEE)) {
            statement.setInt(1, employee.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Shift shift = modelFactory.newShift();
                    shift.setId(rs.getInt("shift_id"));
                    shift.setName(rs.getString("name"));
                    shift.setStartTime(rs.getTime("start_time"));
                    shift.setEndTime(rs.getTime("end_time"));
                    shift.setLocation(rs.getString("location"));
                    shifts.add(shift);
                }
            }
        }
        return shifts;
    }

    private Set<EmployeeTraining> loadTrainings(Connection connection, Employee employee) throws SQLException {
        Set<EmployeeTraining> trainings = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_TRAININGS_FOR_EMPLOYEE)) {
            statement.setInt(1, employee.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Training training = modelFactory.newTraining();
                    training.setId(rs.getInt("training_id"));
                    training.setTitle(rs.getString("title"));
                    // In a full implementation, you would load the full Training object

                    EmployeeTraining et = modelFactory.newEmployeeTraining();
                    et.setEmployee(employee);

                    // Here we re-use the training object we just made
                    et.setTraining(training);

                    et.setCompletionDate(rs.getDate("completion_date"));
                    et.setPassed(rs.getBoolean("passed"));
                    et.setScore(rs.getBigDecimal("score"));

                    trainings.add(et);
                }
            }
        }
        return trainings;
    }
}