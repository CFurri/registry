package cat.uvic.teknos.dam.registry.jdbc.repository;

import cat.uvic.teknos.dam.registry.EmployeeRepository;
import cat.uvic.teknos.dam.registry.RepositoryFactory;
import cat.uvic.teknos.dam.registry.ShiftRepository;
import cat.uvic.teknos.dam.registry.TrainingRepository;
import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.DataSource;
import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.SingleConnectionDataSource;

public class JdbcRepositoryFactory implements RepositoryFactory {
    //Li hem de passar un DataSource
    private final DataSource dataSource;

    public JdbcRepositoryFactory(){
        dataSource = new SingleConnectionDataSource();
    }
    @Override
    public EmployeeRepository getEmployeeRepository() {
        return new JdbcEmployeeRepository(dataSource);
    }

    @Override
    public ShiftRepository getShiftRepository() {
        return null;
    }

    @Override
    public TrainingRepository getTrainingRepository() {
        return null;
    }
}
