package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.datasource.HikariDataSourceAdapter;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Shift;
import cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcShiftRepositoryTest {

    private DataSource dataSource;
    private JdbcShiftRepository repository;
    private final ModelFactory modelFactory = new JdbcModelFactory();

    @BeforeEach
    void setUp() throws SQLException, IOException {
        dataSource = new HikariDataSourceAdapter();
        repository = new JdbcShiftRepository(dataSource);

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
        // Given: a shift with ID 1 exists in data.sql
        // When: we retrieve the shift
        Shift shift = repository.get(1);

        // Then: the shift is not null and has the correct data
        assertNotNull(shift);
        assertEquals(1, shift.getId());
        assertEquals("Matí", shift.getName());

        // And: eager loading of employees worked
        assertNotNull(shift.getEmployees());
        assertEquals(2, shift.getEmployees().size());
    }

    @Test
    void saveInsertTest() {
        // Given: a new shift object
        Shift newShift = modelFactory.newShift();
        newShift.setName("Weekend Shift");
        newShift.setStartTime(Time.valueOf("10:00:00"));
        newShift.setEndTime(Time.valueOf("18:00:00"));
        newShift.setLocation("Warehouse");

        // When: we save the new shift
        repository.save(newShift);

        // Then: the shift gets an ID assigned
        assertTrue(newShift.getId() > 0);

        // And: we can retrieve it from the database
        Shift retrievedShift = repository.get(newShift.getId());
        assertEquals("Weekend Shift", retrievedShift.getName());
    }

    @Test
    void saveUpdateTest() {
        // Given: an existing shift with ID 2 ("Tarda")
        Shift shiftToUpdate = repository.get(2);
        assertNotNull(shiftToUpdate);

        // When: we change its location and save
        shiftToUpdate.setLocation("Remote");
        repository.save(shiftToUpdate);

        // Then: the change is persisted
        Shift retrievedShift = repository.get(2);
        assertEquals("Remote", retrievedShift.getLocation());
    }

    @Test
    void deleteTest() {
        // Given: a shift with ID 3 ("Nit") exists
        Shift shiftToDelete = repository.get(3);
        assertNotNull(shiftToDelete);

        // When: we delete the shift
        repository.delete(shiftToDelete);

        // Then: the shift can no longer be retrieved
        assertNull(repository.get(3));
    }

    @Test
    void getAllTest() {
        // Given: the sample data has 5 shifts
        // When: we get all shifts
        Set<Shift> shifts = repository.getAll();

        // Then: the size of the returned set is 5
        assertEquals(5, shifts.size());
    }

    @Test
    void getByLocationTest() {
        // Given: the sample data has 3 shifts at "Oficina Central"
        // When: we get all shifts for that location
        Set<Shift> shifts = repository.getByLocation("Oficina Central");

        // Then: the size of the returned set is 3
        assertEquals(4, shifts.size());
    }
}