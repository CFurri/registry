package cat.uvic.teknos.registry.models;

import java.sql.Date;
import java.util.Set;

/**
 * Model interface for an Employee.
 * It's designed to be compatible with both JPA and JDBC implementations.
 */
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

    // hire_date (java.sql.Date is compatible with both JDBC and JPA for SQL DATE)
    Date getHireDate();
    void setHireDate(Date hireDate);
    
    // Relation 1:1 with EmployeeDetail
    // Each employee has one detail record.
    EmployeeDetail getEmployeeDetail();
    void setEmployeeDetail(EmployeeDetail employeeDetail);
    
    // Relation 1:N with TimeLog
    // An employee can have multiple time logs.
    Set<TimeLog> getTimeLogs();
    void setTimeLogs(Set<TimeLog> timeLogs);
    
    // Relation N:M with Shift
    // An employee can have multiple shifts. The join table is handled in the background.
    Set<Shift> getShifts();
    void setShifts(Set<Shift> shifts);

    // Relation N:M with Training (via EmployeeTraining)
    // This returns a Set of the "link" objects, which is necessary
    // to access attributes of the relationship like completion_date and score.
    Set<EmployeeTraining> getEmployeeTrainings();
    void setEmployeeTrainings(Set<EmployeeTraining> trainings);
}