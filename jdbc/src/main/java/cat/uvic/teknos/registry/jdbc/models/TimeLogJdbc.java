package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.Employee; // Corrected import path
import cat.uvic.teknos.registry.models.TimeLog; // Corrected import path
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimeLogJdbc implements TimeLog {
    private int id;
    private Date logDate;
    private Time checkInTime;
    private Time checkOutTime;
    private BigDecimal totalHours;
    private boolean isLate;
    private boolean isRemote;

    private Employee employee;
}