package cat.uvic.teknos.registry.repositories;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.EmployeeTraining;

import java.util.Set;

public interface EmployeeRepository extends Repository<Integer, Employee> {

    /**
     * Finds an employee by their unique email address.
     * @param email The email to search for.
     * @return The employee, or null if not found.
     */
    Employee getByEmail(String email);

    /**
     * Retrieves all training details (like score and completion date) for a specific employee.
     * This is how you access the data from the EMPLOYEE_TRAINING join table.
     * @param employee The employee whose training details are to be retrieved.
     * @return A set of EmployeeTraining objects.
     */
    Set<EmployeeTraining> getTrainingDetailsForEmployee(Employee employee);
}