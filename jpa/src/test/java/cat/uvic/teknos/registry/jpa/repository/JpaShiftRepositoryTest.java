package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.jpa.models.JpaModelFactory;
import cat.uvic.teknos.registry.jpa.models.JpaShift;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Shift;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Time;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaShiftRepositoryTest {

    private EntityManagerFactory emf;
    private JpaShiftRepository repository;
    private final ModelFactory modelFactory = new JpaModelFactory();

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("registry-h2");
        repository = new JpaShiftRepository(emf);
    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void saveAndGetTest() {
        // Given a new shift object
        Shift newShift = modelFactory.newShift();
        newShift.setName("Night Shift");
        newShift.setLocation("Warehouse");
        newShift.setStartTime(Time.valueOf("22:00:00"));
        newShift.setEndTime(Time.valueOf("06:00:00"));

        // When we save it
        repository.save(newShift);

        // Then it receives an ID and can be retrieved
        assertTrue(newShift.getId() > 0);
        Shift retrieved = repository.get(newShift.getId());
        assertNotNull(retrieved);
        assertEquals("Night Shift", retrieved.getName());
    }

    @Test
    void getAllAndByLocationTest() {
        // Given several shifts are persisted manually
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // FIX: Use standard object creation instead of double brace initialization
            JpaShift shift1 = new JpaShift();
            shift1.setName("Morning");
            shift1.setLocation("Office");
            em.persist(shift1);

            JpaShift shift2 = new JpaShift();
            shift2.setName("Evening");
            shift2.setLocation("Office");
            em.persist(shift2);

            JpaShift shift3 = new JpaShift();
            shift3.setName("Remote");
            shift3.setLocation("Remote");
            em.persist(shift3);

            em.getTransaction().commit();
        }

        // When we get all shifts, the count is correct
        Set<Shift> allShifts = repository.getAll();
        assertEquals(3, allShifts.size());

        // When we search by a specific location, the count is correct
        Set<Shift> officeShifts = repository.getByLocation("Office");
        assertEquals(2, officeShifts.size());
    }

    @Test
    void deleteTest() {
        // Given a shift is created in the database
        int shiftId;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            JpaShift shift = new JpaShift();
            shift.setName("Temporary Shift");
            em.persist(shift);
            em.getTransaction().commit();
            shiftId = shift.getId();
        }

        // When we delete it
        Shift shiftToDelete = modelFactory.newShift();
        shiftToDelete.setId(shiftId);
        repository.delete(shiftToDelete);

        // Then it can no longer be found
        assertNull(repository.get(shiftId));
    }
}
