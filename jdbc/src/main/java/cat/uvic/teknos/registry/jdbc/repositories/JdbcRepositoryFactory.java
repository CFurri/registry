package cat.uvic.teknos.registry.jdbc.repositories;

import cat.uvic.teknos.registry.jdbc.datasource.DataSource;
import cat.uvic.teknos.registry.jdbc.datasource.HikariDataSourceAdapter;
import cat.uvic.teknos.registry.repositories.EmployeeRepository;
import cat.uvic.teknos.registry.repositories.RepositoryFactory;
import cat.uvic.teknos.registry.repositories.ShiftRepository;
import cat.uvic.teknos.registry.repositories.TrainingRepository;

public class JdbcRepositoryFactory implements RepositoryFactory {
    private final DataSource dataSource = new HikariDataSourceAdapter(); //Pots canviar-ho per SingleConnection

    @Override
    public EmployeeRepository getEmployeeRepository() {
        return new JdbcEmployeeRepository(dataSource);
    }

    @Override
    public ShiftRepository getShiftRepository() {
        return new JdbcShiftRepository(dataSource);

    }

    @Override
    public TrainingRepository getTrainingRepository() {
        return new JdbcTrainingRepository(dataSource);
    }

}
