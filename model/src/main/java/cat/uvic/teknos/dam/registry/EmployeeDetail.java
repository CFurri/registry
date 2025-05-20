package cat.uvic.teknos.dam.registry;

public interface EmployeeDetail {
    // employee_id (PK i FK)
    int getId();
    void setId(int id);
    
    // Relación con Employee (1:1)
    Employee getEmployee();
    void setEmployee(Employee employee);

    // address
    String getAddress();
    void setAddress(String address);

    // national_id
    String getNationalId();
    void setNationalId(String nationalId);

    // emergency_contact_name
    String getEmergencyContactName();
    void setEmergencyContactName(String name);

    // emergency_contact_phone
    String getEmergencyContactPhone();
    void setEmergencyContactPhone(String phone);
}
