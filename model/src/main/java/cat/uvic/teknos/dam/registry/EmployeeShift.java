package cat.uvic.teknos.dam.registry;

public interface EmployeeShift {
    // employee_id (PK, FK)
    int getEmployeeId();
    void setEmployeeId(int employeeId);
    
    // Relación con Employee
    Employee getEmployee();
    void setEmployee(Employee employee);

    // shift_id (PK, FK)
    int getShiftId();
    void setShiftId(int shiftId);
    
    // Relación con Shift
    Shift getShift();
    void setShift(Shift shift);
}
