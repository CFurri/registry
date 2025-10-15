package cat.uvic.teknos.registry.jpa.repository;

import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.repositories.ShiftRepository;
import cat.uvic.teknos.registry.repositories.TrainingRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaRepositoryFactory implements RepositoryFactory {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("registry");

    @Override
    public EmployeeRepository getEmployeeRepository() {
        return new JpaEmployeeRepository(emf);
    }

    @Override
    public ShiftRepository getShiftRepository() {
        return new JpaShiftRepository(emf);
    }

    @Override
    public TrainingRepository getTrainingRepository() {
        return new JpaTrainingRepository(emf);
    }


}
