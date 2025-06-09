package cat.uvic.teknos.registry.jdbc.datasource;

import java.sql.Connection;

/**
 * Defines the contract for a data source provider.
 */
public interface DataSource {
    /**
     * Attempts to establish a connection with the data source.
     *
     * @return a connection to the data source.
     */
    Connection getConnection();

    /**
     * Closes the data source and releases all underlying resources,
     * such as the connection pool.
     */
    void close();
}