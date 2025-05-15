package cat.uvic.teknos.dam.registry.jdbc.repository.datasource;

import java.sql.Connection;

public interface DataSource {
    Connection getConnection();
}
