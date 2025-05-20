package cat.uvic.teknos.dam.registry;

import java.time.LocalTime;
import java.util.Set;

public interface Shift {
    // shift_id (PK auto-incremental)
    int getId();
    void setId(int id);

    // name
    String getName();
    void setName(String name);

    // start_time (TIME → LocalTime)
    LocalTime getStartTime();
    void setStartTime(LocalTime startTime);

    // end_time (TIME → LocalTime)
    LocalTime getEndTime();
    void setEndTime(LocalTime endTime);

    // location
    String getLocation();
    void setLocation(String location);

    // Relación many-to-many con Employee
    Set<Employee> getEmployees();
    void setEmployees(Set<Employee> employees);
    void addEmployee(Employee employee);
    void removeEmployee(Employee employee);
}
