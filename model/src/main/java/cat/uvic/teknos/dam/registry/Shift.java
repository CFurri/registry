package cat.uvic.teknos.dam.registry;

public interface Shift {
    // Relació many-to-many amb Employee
    List<Employee> getEmployees();
    void setEmployees(List<Employee> employees);
}
