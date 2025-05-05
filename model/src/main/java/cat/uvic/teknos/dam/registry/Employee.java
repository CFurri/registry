package cat.uvic.teknos.dam.registry;

import java.time.LocalDate;

public interface Employee {
    // employee_id (PK auto-incremental)
    int getId();
    void setId(int id);

    // first_name
    String getFirstName();
    void setFirstName(String firstName);

    // last_name
    String getLastName();
    void setLastName(String lastName);

    // email
    String getEmail();
    void setEmail(String email);

    // phone_number
    String getPhoneNumber();
    void setPhoneNumber(String phoneNumber);

    // hire_date (DATE → LocalDate)
    LocalDate getHireDate();
    void setHireDate(LocalDate hireDate);
}
