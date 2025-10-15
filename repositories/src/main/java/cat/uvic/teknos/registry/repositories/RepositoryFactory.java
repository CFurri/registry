package cat.uvic.teknos.registry.repositories;

public interface RepositoryFactory {
    EmployeeRepository getEmployeeRepository();
    ShiftRepository getShiftRepository();
    TrainingRepository getTrainingRepository();

}
