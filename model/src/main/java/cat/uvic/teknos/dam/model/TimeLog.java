package cat.uvic.teknos.dam.model;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TimeLog {
    // log_id (PK autoincremental)
    int getId();
    void setId(int id);

    // employee_id (FK)
    Employee getEmployee();
    void setEmployee(Employee employee);

    // log_date
    LocalDate getLogDate();
    void setLogDate(LocalDate date);

    // check_in_time
    LocalTime getCheckInTime();
    void setCheckInTime(LocalTime time);

    // check_out_time
    LocalTime getCheckOutTime();
    void setCheckOutTime(LocalTime time);

    // total_hours
    double getTotalHours();
    void setTotalHours(double hours);

    // is_late
    boolean isLate();
    void setLate(boolean isLate);

    // is_remote
    boolean isRemote();
    void setRemote(boolean isRemote);

    int getEmployeeId();

    double getHoursWorked();

    String getDescription();
}
