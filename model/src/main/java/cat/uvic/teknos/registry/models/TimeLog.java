package cat.uvic.teknos.registry.models;

import java.sql.Date;
import java.sql.Time;
import java.math.BigDecimal;

public interface TimeLog {
    // log_id (PK auto-incremental)
    int getId();
    void setId(int id);

    // log_date
    Date getLogDate();
    void setLogDate(Date logDate);

    // check_in_time
    Time getCheckInTime();
    void setCheckInTime(Time checkInTime);

    // check_out_time
    Time getCheckOutTime();
    void setCheckOutTime(Time checkOutTime);

    // total_hours (DECIMAL(4,2) -> BigDecimal)
    BigDecimal getTotalHours();
    void setTotalHours(BigDecimal totalHours);

    // is_late
    boolean isLate();
    void setLate(boolean isLate);

    // is_remote
    boolean isRemote();
    void setRemote(boolean isRemote);

    // Relation N:1 with Employee
    Employee getEmployee();
    void setEmployee(Employee employee);
}