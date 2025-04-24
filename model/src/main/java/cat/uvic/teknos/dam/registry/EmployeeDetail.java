package cat.uvic.teknos.dam.registry;

public interface EmployeeDetail {
    // employee_id (PK i FK)
    int getEmployeeId();
    void setEmployeeId(int employeeId);

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
