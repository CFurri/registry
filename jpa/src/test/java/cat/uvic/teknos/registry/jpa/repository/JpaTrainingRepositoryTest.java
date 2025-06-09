package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.jpa.models.JpaModelFactory;
import cat.uvic.teknos.registry.jpa.models.JpaTraining;
import cat.uvic.teknos.registry.models.ModelFactory;
import cat.uvic.teknos.registry.models.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JpaTrainingRepositoryTest {

    private EntityManagerFactory emf;
    private JpaTrainingRepository repository;
    private final ModelFactory modelFactory = new JpaModelFactory();

    @BeforeEach
    void setUp() {
        emf = Persistence.createEntityManagerFactory("registry-h2");
        repository = new JpaTrainingRepository(emf);
    }

    @AfterEach
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void saveAndGetTest() {
        // Given a new training object
        Training newTraining = modelFactory.newTraining();
        newTraining.setTitle("Advanced SQL");
        newTraining.setMandatory(false);
        newTraining.setDurationHours(10);

        // When we save it
        repository.save(newTraining);

        // Then it gets an ID and can be retrieved
        assertTrue(newTraining.getId() > 0);
        Training retrieved = repository.get(newTraining.getId());
        assertNotNull(retrieved);
        assertEquals("Advanced SQL", retrieved.getTitle());
    }

    @Test
    void getAllAndGetMandatoryTest() {
        // Given several training courses are persisted manually
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // FIX: Use standard object creation instead of double brace initialization
            JpaTraining training1 = new JpaTraining();
            training1.setTitle("Mandatory 1");
            training1.setMandatory(true);
            em.persist(training1);

            JpaTraining training2 = new JpaTraining();
            training2.setTitle("Optional 1");
            training2.setMandatory(false);
            em.persist(training2);

            JpaTraining training3 = new JpaTraining();
            training3.setTitle("Mandatory 2");
            training3.setMandatory(true);
            em.persist(training3);

            em.getTransaction().commit();
        }

        // When we get all trainings, the count is correct
        Set<Training> allTrainings = repository.getAll();
        assertEquals(3, allTrainings.size());

        // When we get only mandatory trainings, the count is correct
        Set<Training> mandatoryTrainings = repository.getMandatoryTrainings();
        assertEquals(2, mandatoryTrainings.size());
    }

    @Test
    void deleteTest() {
        // Given a training exists in the database
        int trainingId;
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            JpaTraining training = new JpaTraining();
            training.setTitle("Obsolete Course");
            em.persist(training);
            em.getTransaction().commit();
            trainingId = training.getId();
        }

        // When we delete it
        Training trainingToDelete = modelFactory.newTraining();
        trainingToDelete.setId(trainingId);
        repository.delete(trainingToDelete);

        // Then it can no longer be found
        assertNull(repository.get(trainingId));
    }
}
