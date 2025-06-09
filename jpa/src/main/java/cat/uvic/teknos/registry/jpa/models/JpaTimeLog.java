package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.Employee;
import cat.uvic.teknos.registry.models.TimeLog;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name = "TIME_LOG")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class JpaTimeLog implements TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private int id;

    @Column(name = "log_date")
    private Date logDate;

    @Column(name = "check_in_time")
    private Time checkInTime;

    @Column(name = "check_out_time")
    private Time checkOutTime;

    @Column(name = "total_hours")
    private BigDecimal totalHours;

    @Column(name = "is_late")
    private boolean isLate;

    @Column(name = "is_remote")
    private boolean isRemote;

    @ManyToOne(targetEntity = JpaEmployee.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;
}