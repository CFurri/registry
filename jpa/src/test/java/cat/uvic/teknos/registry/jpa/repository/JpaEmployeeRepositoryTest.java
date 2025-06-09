package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.jpa.models.JpaEmployee;
import cat.uvic.teknos.registry.jpa.models.JpaModelFactory;
import cat.uvic.teknos.registry.jpa.models.JpaShift;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Shift;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaEmployeeRepositoryTest {

    private EntityManagerFactory emf;
    private JpaEmployeeRepository repository;
    private final ModelFactory modelFactory = new JpaModelFactory();

    @BeforeEach
    void setUp() {
        // Creates a new, empty in-memory database schema for each test
        emf = Persistence.createEntityManagerFactory("registry-h2");
        repository = new JpaEmployeeRepository(emf);
    }

    @AfterEach
    void tearDown() {
        // Closes the factory, which drops the in-memory database schema
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void saveInsertTest() {
        // Given a new, unsaved Employee object
        Employee newEmployee = modelFactory.newEmployee();
        newEmployee.setFirstName("Laura");
        newEmployee.setLastName("Vila");
        newEmployee.setEmail("laura.vila@test.com");
        newEmployee.setHireDate(Date.valueOf("2024-02-15"));

        // When we save the employee
        repository.save(newEmployee);

        // Then we can retrieve it from the database and verify its data
        Employee retrieved = repository.getByEmail("laura.vila@test.com");
        assertNotNull(retrieved);
        assertTrue(retrieved.getId() > 0);
        assertEquals("Laura", retrieved.getFirstName());
    }

    @Test
    void saveUpdateTest() {
        // Given an existing employee in the database, created manually
        int employeeId;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            JpaEmployee employeeToUpdate = new JpaEmployee();
            employeeToUpdate.setFirstName("Original");
            employeeToUpdate.setEmail("original@test.com");
            em.persist(employeeToUpdate);
            em.getTransaction().commit();
            employeeId = employeeToUpdate.getId();
        }

        // When we fetch it, modify it, and save it using the repository
        Employee employeeToUpdate = repository.get(employeeId);
        employeeToUpdate.setFirstName("Updated");
        repository.save(employeeToUpdate);

        // Then the changes are persisted
        Employee retrieved = repository.get(employeeId);
        assertEquals("Updated", retrieved.getFirstName());
    }

    @Test
    void deleteTest() {
        // Given an existing employee in the database
        int employeeId;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            JpaEmployee employee = new JpaEmployee();
            employee.setEmail("todelete@test.com");
            em.persist(employee);
            em.getTransaction().commit();
            employeeId = employee.getId();
        }
        Employee employeeToDelete = modelFactory.newEmployee();
        employeeToDelete.setId(employeeId);

        // When we delete the employee
        repository.delete(employeeToDelete);

        // Then it can no longer be found
        assertNull(repository.get(employeeId));
    }

    @Test
    void getAllAndGetByIdTest() {
        // Given a database populated with two employees
        int firstId;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            JpaEmployee e1 = new JpaEmployee(); e1.setEmail("e1@test.com");
            JpaEmployee e2 = new JpaEmployee(); e2.setEmail("e2@test.com");
            em.persist(e1);
            em.persist(e2);
            em.getTransaction().commit();
            firstId = e1.getId();
        }

        // When we get all employees
        Set<Employee> allEmployees = repository.getAll();

        // Then the result set contains both employees
        assertEquals(2, allEmployees.size());

        // And when we get one by its ID, it is returned correctly
        Employee singleEmployee = repository.get(firstId);
        assertNotNull(singleEmployee);
        assertEquals("e1@test.com", singleEmployee.getEmail());
    }
}