package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.Employee; // Corrected import path
import cat.uvic.teknos.registry.models.Shift; // Corrected import path
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftJdbc implements Shift {
    private int id;
    private String name;
    private Time startTime;
    private Time endTime;
    private String location;

    private Set<Employee> employees;
}