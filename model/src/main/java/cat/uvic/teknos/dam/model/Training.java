package cat.uvic.teknos.dam.model;

import java.util.Set;

public interface Training {
    // training_id (PK autoincremental)
    int getId();
    void setId(int id);

    // title
    String getTitle();
    void setTitle(String title);

    // description
    String getDescription();
    void setDescription(String description);

    // duration_hours
    int getDurationHours();
    void setDurationHours(int durationHours);

    // mandatory
    boolean isMandatory();
    void setMandatory(boolean mandatory);
    
    // Relación many-to-many con Employee
    Set<Employee> getEmployees();
    void setEmployees(Set<Employee> employees);
    void addEmployee(Employee employee);
    void removeEmployee(Employee employee);
}
