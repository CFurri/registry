package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.jpa.exceptions.CrudException;
import cat.uvic.teknos.registry.jpa.exceptions.EntityManagerException;
import cat.uvic.teknos.registry.jpa.models.JpaShift;
import cat.uvic.teknos.registry.models.Shift;
import cat.uvic.teknos.registry.repositories.ShiftRepository;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class JpaShiftRepository implements ShiftRepository {

    private final EntityManagerFactory emf;

    public JpaShiftRepository(EntityManagerFactory emf) {
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
    public void save(Shift value) {
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
            throw new CrudException("Error saving Shift.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Shift value) {
        if (value == null || value.getId() <= 0) return;

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Shift managedShift = em.find(JpaShift.class, value.getId());
            if (managedShift != null) {
                em.remove(managedShift);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new CrudException("Error deleting Shift with ID: " + value.getId(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Shift get(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT s FROM JpaShift s LEFT JOIN FETCH s.employees WHERE s.id = :id", Shift.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new CrudException("Error retrieving Shift with ID: " + id, e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Set<Shift> getAll() {
        EntityManager em = getEntityManager();
        try {
            var list = em.createQuery("SELECT DISTINCT s FROM JpaShift s LEFT JOIN FETCH s.employees", Shift.class)
                    .getResultList();
            return new HashSet<>(list);
        } catch (Exception e) {
            throw new CrudException("Error retrieving all Shifts.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Set<Shift> getByLocation(String location) {
        EntityManager em = getEntityManager();
        try {
            var list = em.createQuery("SELECT DISTINCT s FROM JpaShift s LEFT JOIN FETCH s.employees WHERE s.location = :location", Shift.class)
                    .setParameter("location", location)
                    .getResultList();
            return new HashSet<>(list);
        } catch (Exception e) {
            throw new CrudException("Error retrieving Shifts by location: " + location, e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}