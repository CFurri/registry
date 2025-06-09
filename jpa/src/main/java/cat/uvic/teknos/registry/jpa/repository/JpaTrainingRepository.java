package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.jpa.exceptions.CrudException;
import cat.uvic.teknos.registry.jpa.exceptions.EntityManagerException;
import cat.uvic.teknos.registry.jpa.models.JpaTraining;
import cat.uvic.teknos.registry.models.Training;
import cat.uvic.teknos.registry.repositories.TrainingRepository;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class JpaTrainingRepository implements TrainingRepository {

    private final EntityManagerFactory emf;

    public JpaTrainingRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        try {
            return emf.createEntityManager();
        } catch (Exception e) {
            throw new EntityManagerException("Failed to create EntityManager.", e);
        }
    }

    @Override
    public void save(Training value) {
        if (value == null) return;

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            if (value.getId() <= 0) {
                em.persist(value);
            } else {
                em.merge(value);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new CrudException("Error saving Training.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Training value) {
        if (value == null || value.getId() <= 0) return;

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Training managedTraining = em.find(JpaTraining.class, value.getId());
            if (managedTraining != null) {
                em.remove(managedTraining);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new CrudException("Error deleting Training with ID: " + value.getId(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Training get(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT t FROM JpaTraining t LEFT JOIN FETCH t.employeeTrainings WHERE t.id = :id", Training.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new CrudException("Error retrieving Training with ID: " + id, e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Set<Training> getAll() {
        EntityManager em = getEntityManager();
        try {
            var list = em.createQuery("SELECT DISTINCT t FROM JpaTraining t LEFT JOIN FETCH t.employeeTrainings", Training.class)
                    .getResultList();
            return new HashSet<>(list);
        } catch (Exception e) {
            throw new CrudException("Error retrieving all Trainings.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Set<Training> getMandatoryTrainings() {
        EntityManager em = getEntityManager();
        try {
            var list = em.createQuery("SELECT DISTINCT t FROM JpaTraining t LEFT JOIN FETCH t.employeeTrainings WHERE t.mandatory = true", Training.class)
                    .getResultList();
            return new HashSet<>(list);
        } catch (Exception e) {
            throw new CrudException("Error retrieving mandatory Trainings.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}