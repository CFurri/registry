package cat.uvic.teknos.registry.jdbc.models;

import cat.uvic.teknos.registry.models.*;

/**
 * Implements the ModelFactory to create instances of the concrete JDBC models.
 * This decouples the rest of the application from knowing about the specific
 * implementation classes (e.g., EmployeeJdbc).
 */
public class JdbcModelFactory implements ModelFactory {

    @Override
    public Employee newEmployee() {
        return new EmployeeJdbc();
    }

    @Override
    public EmployeeDetail newEmployeeDetail() {
        return new EmployeeDetailJdbc();
    }

    @Override
    public EmployeeTraining newEmployeeTraining() {
        return new EmployeeTrainingJdbc();
    }

    @Override
    public Shift newShift() {
        return new ShiftJdbc();
    }

    @Override
    public TimeLog newTimeLog() {
        return new TimeLogJdbc();
    }

    @Override
    public Training newTraining() {
        return new TrainingJdbc();
    }
}