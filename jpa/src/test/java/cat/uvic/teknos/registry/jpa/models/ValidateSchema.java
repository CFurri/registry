package cat.uvic.teknos.registry.jpa.models;

import jakarta.persistence.Persistence;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidateSchema {

    @Test
    void validateSchema() {
        var emf = Persistence.createEntityManagerFactory("registry_test");
        var em = emf.createEntityManager();
        assertNotNull(em);
        em.getTransaction().begin();
        assertTrue(em.getTransaction().isActive());
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}