package cat.uvic.teknos.dam.registry;

import java.util.List;

public interface Shift {
    int getShiftId();

    void setShiftId(int shiftId);

    String getName();

    void setName(String name);

    String getStartTime();

    void setStartTime(String startTime);

    String getEndTime();

    void setEndTime(String endTime);

    String getLocation();

    void setLocation(String location);

    // Relació many-to-many amb Employee
    List<Employee> getEmployees();
    void setEmployees(List<Employee> employees);
}


