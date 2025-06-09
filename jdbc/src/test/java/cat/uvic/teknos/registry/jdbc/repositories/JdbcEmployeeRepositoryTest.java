package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.datasource.HikariDataSourceAdapter;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.jdbc.models.JdbcModelFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcEmployeeRepositoryTest {

    private DataSource dataSource;
    private JdbcEmployeeRepository repository;

    @BeforeEach
    void setUp() throws SQLException, IOException {
        dataSource = new HikariDataSourceAdapter(); // Uses test datasource.properties
        repository = new JdbcEmployeeRepository(dataSource);

        // Load DDL script
        var schema = new String(Objects.requireNonNull(this.getClass().getResourceAsStream("/schema.sql")).readAllBytes());
        // Load sample data script
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
            // Simple way to tear down an H2 in-memory db for the next test
            statement.execute("DROP ALL OBJECTS");
        }
    }

    @Test
    void getTest() {
        // Given: an employee with ID 1 exists in data.sql
        // When: we retrieve the employee
        Employee employee = repository.get(1);

        // Then: the employee is not null and has the correct data
        assertNotNull(employee);
        assertEquals(1, employee.getId());
        assertEquals("Joan", employee.getFirstName());

        // And: eager loading worked
        assertNotNull(employee.getShifts());
        assertFalse(employee.getShifts().isEmpty());
        assertEquals(1, employee.getShifts().size());
    }

    @Test
    void saveInsertTest() {
        // Given: a new employee object
        ModelFactory modelFactory = new JdbcModelFactory();
        Employee newEmployee = modelFactory.newEmployee();
        newEmployee.setFirstName("Peter");
        newEmployee.setLastName("Jones");
        newEmployee.setEmail("peter.jones@test.com");
        newEmployee.setHireDate(Date.valueOf("2024-01-01"));

        // When: we save the new employee
        repository.save(newEmployee);

        // Then: the employee gets an ID assigned
        assertTrue(newEmployee.getId() > 0);

        // And: we can retrieve it from the database
        Employee retrievedEmployee = repository.get(newEmployee.getId());
        assertEquals("Peter", retrievedEmployee.getFirstName());
    }

    @Test
    void saveUpdateTest() {
        // Given: an existing employee with ID 2
        Employee employeeToUpdate = repository.get(2);
        assertNotNull(employeeToUpdate);

        // When: we change their last name and save
        employeeToUpdate.setLastName("Williams");
        repository.save(employeeToUpdate);

        // Then: the change is persisted
        Employee retrievedEmployee = repository.get(2);
        assertEquals("Williams", retrievedEmployee.getLastName());
    }

    @Test
    void deleteTest() {
        // Given: an employee with ID 1 exists
        Employee employeeToDelete = repository.get(1);
        assertNotNull(employeeToDelete);

        // When: we delete the employee
        repository.delete(employeeToDelete);

        // Then: the employee can no longer be retrieved
        assertNull(repository.get(1));
    }

    @Test
    void getAllTest() {
        // Given: the sample data has 2 employees
        // When: we get all employees
        Set<Employee> employees = repository.getAll();

        // Then: the size of the returned set is 2
        assertEquals(5, employees.size());
    }
}