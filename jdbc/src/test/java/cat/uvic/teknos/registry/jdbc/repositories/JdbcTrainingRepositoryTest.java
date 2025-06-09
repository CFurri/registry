package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.datasource.HikariDataSourceAdapter;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Training;
import cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcTrainingRepositoryTest {

    private DataSource dataSource;
    private JdbcTrainingRepository repository;
    private final ModelFactory modelFactory = new JdbcModelFactory();

    @BeforeEach
    void setUp() throws SQLException, IOException {
        dataSource = new HikariDataSourceAdapter();
        repository = new JdbcTrainingRepository(dataSource);

        var schema = new String(Objects.requireNonNull(this.getClass().getResourceAsStream("/schema.sql")).readAllBytes());
        var data = new String(Objects.requireNonNull(this.getClass().getResourceAsStream("/data.sql")).readAllBytes());

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(schema);
            statement.execute(data);
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DROP ALL OBJECTS");
        }
    }

    @Test
    void getTest() {
        // Given: a training with ID 1 exists in data.sql
        // When: we retrieve the training
        Training training = repository.get(1);

        // Then: the training is not null and has the correct data
        assertNotNull(training);
        assertEquals(1, training.getId());
        assertEquals("Seguretat a la feina", training.getTitle());

        // And: eager loading of employee trainings worked
        assertNotNull(training.getEmployeeTrainings());
        assertEquals(5, training.getEmployeeTrainings().size());
    }

    @Test
    void saveInsertTest() {
        // Given: a new training object
        Training newTraining = modelFactory.newTraining();
        newTraining.setTitle("Advanced Git");
        newTraining.setDescription("Mastering git workflows");
        newTraining.setDurationHours(8);
        newTraining.setMandatory(false);

        // When: we save the new training
        repository.save(newTraining);

        // Then: the training gets an ID assigned
        assertTrue(newTraining.getId() > 0);

        // And: we can retrieve it from the database
        Training retrievedTraining = repository.get(newTraining.getId());
        assertEquals("Advanced Git", retrievedTraining.getTitle());
    }

    @Test
    void saveUpdateTest() {
        // Given: an existing training with ID 3 ("Trello avançat")
        Training trainingToUpdate = repository.get(3);
        assertNotNull(trainingToUpdate);

        // When: we change its duration and save
        trainingToUpdate.setDurationHours(4);
        repository.save(trainingToUpdate);

        // Then: the change is persisted
        Training retrievedTraining = repository.get(3);
        assertEquals(4, retrievedTraining.getDurationHours());
    }

    @Test
    void deleteTest() {
        // Given: a training with ID 4 ("Comunicació efectiva") exists
        Training trainingToDelete = repository.get(4);
        assertNotNull(trainingToDelete);

        // When: we delete the training
        repository.delete(trainingToDelete);

        // Then: the training can no longer be retrieved
        assertNull(repository.get(4));
    }

    @Test
    void getAllTest() {
        // Given: the sample data has 5 trainings
        // When: we get all trainings
        Set<Training> trainings = repository.getAll();

        // Then: the size of the returned set is 5
        assertEquals(5, trainings.size());
    }

    @Test
    void getMandatoryTrainingsTest() {
        // Given: the sample data has 3 mandatory trainings
        // When: we get all mandatory trainings
        Set<Training> trainings = repository.getMandatoryTrainings();

        // Then: the size of the returned set is 3
        assertEquals(3, trainings.size());
    }
}