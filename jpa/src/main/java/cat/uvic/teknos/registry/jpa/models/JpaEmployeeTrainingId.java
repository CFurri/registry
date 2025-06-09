package cat.uvic.teknos.registry.jpa.models;

import lombok.EqualsAndHashCode;
import java.io.Serializable;

// This class represents the composite primary key
@EqualsAndHashCode
public class JpaEmployeeTrainingId implements Serializable {
    private int employee;
    private int training;
}