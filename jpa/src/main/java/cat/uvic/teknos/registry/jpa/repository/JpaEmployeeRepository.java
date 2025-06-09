package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.jpa.exceptions.CrudException;
import cat.uvic.teknos.registry.jpa.exceptions.EntityManagerException;
import cat.uvic.teknos.registry.jpa.models.JpaEmployee;
import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.EmployeeTraining;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class JpaEmployeeRepository implements EmployeeRepository {

    private final EntityManagerFactory emf;

    public JpaEmployeeRepository(EntityManagerFactory emf) {
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
    public void save(Employee value) {
        if (value == null) return;

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            JpaEmployee employeeJpa = (JpaEmployee) value;

            // Manually manage bidirectional side for EmployeeDetail
            if (employeeJpa.getEmployeeDetail() != null) {
                employeeJpa.getEmployeeDetail().setEmployee(employeeJpa);
            }

            if (employeeJpa.getId() == 0) {
                em.persist(employeeJpa);
            } else {
                em.merge(employeeJpa);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new CrudException("Error saving Employee.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void delete(Employee value) {
        if (value == null || value.getId() <= 0) return;

        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Employee managedEmployee = em.find(JpaEmployee.class, value.getId());
            if (managedEmployee != null) {
                em.remove(managedEmployee);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw new CrudException("Error deleting Employee with ID: " + value.getId(), e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Employee get(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM JpaEmployee e LEFT JOIN FETCH e.shifts LEFT JOIN FETCH e.employeeTrainings WHERE e.id = :id", Employee.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new CrudException("Error retrieving Employee with ID: " + id, e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Set<Employee> getAll() {
        EntityManager em = getEntityManager();
        try {
            var list = em.createQuery("SELECT DISTINCT e FROM JpaEmployee e LEFT JOIN FETCH e.shifts LEFT JOIN FETCH e.employeeTrainings", Employee.class)
                    .getResultList();
            return new HashSet<>(list);
        } catch (Exception e) {
            throw new CrudException("Error retrieving all Employees.", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Employee getByEmail(String email) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT e FROM JpaEmployee e LEFT JOIN FETCH e.shifts LEFT JOIN FETCH e.employeeTrainings WHERE e.email = :email", Employee.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            throw new CrudException("Error retrieving Employee with email: " + email, e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public Set<EmployeeTraining> getTrainingDetailsForEmployee(Employee employee) {
        Employee managedEmployee = get(employee.getId());
        return managedEmployee != null ? managedEmployee.getEmployeeTrainings() : new HashSet<>();
    }
}