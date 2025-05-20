package cat.uvic.teknos.dam.registry;

import java.time.LocalDate;
import java.util.Set;

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
    
    // Relación 1:1 con EmployeeDetail
    EmployeeDetail getEmployeeDetail();
    void setEmployeeDetail(EmployeeDetail employeeDetail);
    
    // Relación 1:N con TimeLog
    Set<TimeLog> getTimeLogs();
    void setTimeLogs(Set<TimeLog> timeLogs);
    void addTimeLog(TimeLog timeLog);
    void removeTimeLog(TimeLog timeLog);
    
    // Relación N:M con Shift
    Set<Shift> getShifts();
    void setShifts(Set<Shift> shifts);
    void addShift(Shift shift);
    void removeShift(Shift shift);
    
    // Relación N:M con Training
    Set<Training> getTrainings();
    void setTrainings(Set<Training> trainings);
    void addTraining(Training training);
    void removeTraining(Training training);
}
