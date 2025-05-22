package cat.uvic.teknos.dam.registry;

public interface RepositoryFactory {
    EmployeeRepository getEmployeeRepository();
    ShiftRepository getShiftRepository();
    TrainingRepository getTrainingRepository();

}
