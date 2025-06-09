package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.exceptions.CrudException;
import cat.uvic.teknos.registry.jdbc.exceptions.TransactionException;
import cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.EmployeeTraining;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Training;
import cat.uvic.teknos.registry.repositories.TrainingRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class JdbcTrainingRepository implements TrainingRepository {

    private final DataSource dataSource;
    private final ModelFactory modelFactory = new JdbcModelFactory();

    // SQL Statements
    private static final String INSERT_TRAINING = "INSERT INTO TRAINING (title, description, duration_hours, mandatory) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_TRAINING = "UPDATE TRAINING SET title = ?, description = ?, duration_hours = ?, mandatory = ? WHERE training_id = ?";
    private static final String DELETE_ASSOCIATIONS = "DELETE FROM EMPLOYEE_TRAINING WHERE training_id = ?";
    private static final String DELETE_TRAINING = "DELETE FROM TRAINING WHERE training_id = ?";
    private static final String GET_TRAINING_BY_ID = "SELECT * FROM TRAINING WHERE training_id = ?";
    private static final String GET_ALL_TRAININGS = "SELECT * FROM TRAINING";
    private static final String GET_MANDATORY_TRAININGS = "SELECT * FROM TRAINING WHERE mandatory = TRUE";
    private static final String GET_EMPLOYEE_TRAININGS_FOR_TRAINING = "SELECT et.*, e.first_name, e.last_name FROM EMPLOYEE_TRAINING et JOIN EMPLOYEE e ON et.employee_id = e.employee_id WHERE et.training_id = ?";


    public JdbcTrainingRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Training training) {
        if (training == null) return;
        try (Connection connection = dataSource.getConnection()) {
            if (training.getId() <= 0) {
                insert(connection, training);
            } else {
                update(connection, training);
            }
        } catch (SQLException e) {
            throw new CrudException("Error saving training.", e);
        }
    }

    private void insert(Connection connection, Training training) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(INSERT_TRAINING, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, training.getTitle());
            statement.setString(2, training.getDescription());
            statement.setInt(3, training.getDurationHours());
            statement.setBoolean(4, training.isMandatory());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    training.setId(keys.getInt(1));
                } else {
                    throw new CrudException("Failed to retrieve generated key for new training.");
                }
            }
        }
    }

    private void update(Connection connection, Training training) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE_TRAINING)) {
            statement.setString(1, training.getTitle());
            statement.setString(2, training.getDescription());
            statement.setInt(3, training.getDurationHours());
            statement.setBoolean(4, training.isMandatory());
            statement.setInt(5, training.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new CrudException("Could not update training with ID " + training.getId() + ". It may not exist.");
            }
        }
    }


    @Override
    public void delete(Training training) {
        if (training == null || training.getId() <= 0) return;

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement assocStatement = connection.prepareStatement(DELETE_ASSOCIATIONS);
                 PreparedStatement trainingStatement = connection.prepareStatement(DELETE_TRAINING)) {

                assocStatement.setInt(1, training.getId());
                assocStatement.executeUpdate();

                trainingStatement.setInt(1, training.getId());
                trainingStatement.executeUpdate();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new TransactionException("Error during delete transaction for training, rollback initiated.", e);
            }
        } catch (SQLException e) {
            throw new CrudException("Failed to get connection for training delete.", e);
        }
    }

    @Override
    public Training get(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_TRAINING_BY_ID)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapAndFetchEager(connection, resultSet);
                }
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving training with ID: " + id, e);
        }
        return null;
    }

    @Override
    public Set<Training> getAll() {
        Set<Training> trainings = new HashSet<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_ALL_TRAININGS)) {
            while (resultSet.next()) {
                trainings.add(mapAndFetchEager(connection, resultSet));
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving all trainings.", e);
        }
        return trainings;
    }

    @Override
    public Set<Training> getMandatoryTrainings() {
        Set<Training> trainings = new HashSet<>();
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_MANDATORY_TRAININGS)) {
            while (resultSet.next()) {
                trainings.add(mapAndFetchEager(connection, resultSet));
            }
        } catch (SQLException e) {
            throw new CrudException("Error retrieving mandatory trainings.", e);
        }
        return trainings;
    }

    private Training mapAndFetchEager(Connection connection, ResultSet resultSet) throws SQLException {
        Training training = modelFactory.newTraining();
        training.setId(resultSet.getInt("training_id"));
        training.setTitle(resultSet.getString("title"));
        training.setDescription(resultSet.getString("description"));
        training.setDurationHours(resultSet.getInt("duration_hours"));
        training.setMandatory(resultSet.getBoolean("mandatory"));

        training.setEmployeeTrainings(loadEmployeeTrainings(connection, training));

        return training;
    }

    private Set<EmployeeTraining> loadEmployeeTrainings(Connection connection, Training training) throws SQLException {
        Set<EmployeeTraining> employeeTrainings = new HashSet<>();
        try (PreparedStatement statement = connection.prepareStatement(GET_EMPLOYEE_TRAININGS_FOR_TRAINING)) {
            statement.setInt(1, training.getId());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    EmployeeTraining et = modelFactory.newEmployeeTraining();

                    Employee employee = modelFactory.newEmployee();
                    employee.setId(rs.getInt("employee_id"));
                    employee.setFirstName(rs.getString("first_name"));
                    employee.setLastName(rs.getString("last_name"));

                    et.setEmployee(employee);
                    et.setTraining(training);
                    et.setCompletionDate(rs.getDate("completion_date"));
                    et.setPassed(rs.getBoolean("passed"));
                    et.setScore(rs.getBigDecimal("score"));

                    employeeTrainings.add(et);
                }
            }
        }
        return employeeTrainings;
    }
}