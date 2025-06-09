package cat.uvic.teknos.registry.jpa.models;

import cat.uvic.teknos.registry.models.*;

public class JpaModelFactory implements ModelFactory {

    @Override
    public Employee newEmployee() {
        return new JpaEmployee();
    }

    @Override
    public EmployeeDetail newEmployeeDetail() {
        return new JpaEmployeeDetail();
    }

    @Override
    public EmployeeTraining newEmployeeTraining() {
        return new JpaEmployeeTraining();
    }

    @Override
    public Shift newShift() {
        return new JpaShift();
    }

    @Override
    public TimeLog newTimeLog() {
        return new JpaTimeLog();
    }

    @Override
    public Training newTraining() {
        return new JpaTraining();
    }
}
