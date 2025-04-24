package cat.uvic.teknos.dam.registry;

import java.util.List;

public interface Shift {
    // Relació many-to-many amb Employee
    List<Employee> getEmployees();
    void setEmployees(List<Employee> employees);
}


