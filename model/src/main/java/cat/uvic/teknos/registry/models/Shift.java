package cat.uvic.teknos.registry.models;

import java.sql.Time;
import java.util.Set;

public interface Shift {
    // shift_id (PK auto-incremental)
    int getId();
    void setId(int id);

    // name
    String getName();
    void setName(String name);

    // start_time
    Time getStartTime();
    void setStartTime(Time startTime);

    // end_time
    Time getEndTime();
    void setEndTime(Time endTime);

    // location
    String getLocation();
    void setLocation(String location);

    // Relation N:M with Employee
    Set<Employee> getEmployees();
    void setEmployees(Set<Employee> employees);
}