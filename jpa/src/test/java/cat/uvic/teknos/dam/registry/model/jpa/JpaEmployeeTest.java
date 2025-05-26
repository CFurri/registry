package cat.uvic.teknos.dam.registry.model.jpa;

import cat.uvic.teknos.dam.model.Employee;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class JpaEmployeeTest {

    @Test
    @DisplayName("Shows how to insert a new employee using Jpa")
    void insertEmployee(){
        try (var entityManagerFactory = Persistence.createEntityManagerFactory("registry.test")){
             Employee employee = new JpaEmployee();
             employee.setFirstName("Jorge");

             var entityManager = entityManagerFactory.createEntityManager();
             entityManager.getTransaction().begin();
             entityManager.persist(employee);
             entityManager.getTransaction().commit();

             var entityManager2 = entityManagerFactory.createEntityManager();
             entityManager2.getTransaction().begin();
             var modifiedEmployee = new JpaEmployee();
             modifiedEmployee.setId(1);
             modifiedEmployee.setFirstName("Ferran");
             entityManager.merge(modifiedEmployee);
             entityManager.getTransaction().commit();


        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}