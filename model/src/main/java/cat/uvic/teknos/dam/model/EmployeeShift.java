package cat.uvic.teknos.dam.model;

public interface EmployeeShift {
    // employee_id (PK, FK)
    int getPersonId();
    void setPersonId(int personId);
    
    // Relación con Employee
    Employee getEmployee();
    void setEmployee(Employee employee);

    // shift_id (PK, FK)
    int getScheduleId();
    void setScheduleId(int scheduleId);
    
    // Relación con Shift
    Shift getShift();
    void setShift(Shift shift);
}
