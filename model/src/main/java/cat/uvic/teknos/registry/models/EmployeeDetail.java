package cat.uvic.teknos.registry.models;

public interface EmployeeDetail {
    // In a 1:1 relationship, the PK is also the FK.
    // It corresponds to the associated Employee's ID.
    int getId();
    void setId(int id);

    // address
    String getAddress();
    void setAddress(String address);

    // national_id
    String getNationalId();
    void setNationalId(String nationalId);

    // emergency_contact_name
    String getEmergencyContactName();
    void setEmergencyContactName(String emergencyContactName);

    // emergency_contact_phone
    String getEmergencyContactPhone();
    void setEmergencyContactPhone(String emergencyContactPhone);

    // Relation 1:1 with Employee
    Employee getEmployee();
    void setEmployee(Employee employee);
}