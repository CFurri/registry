package cat.uvic.teknos.registry.jdbc.datasource;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple integration test for HikariDataSourceAdapter using an H2 in-memory database.
 */
class HikariDataSourceAdapterTest {

    @Test
    void shouldCreateDataSourceAndGetConnection() {
        HikariDataSourceAdapter dataSource = null;
        try {
            // 1. Instantiation Test: Check if the constructor works with the test properties file.
            dataSource = new HikariDataSourceAdapter();
            assertNotNull(dataSource, "DataSource should not be null after instantiation.");

            // 2. Connection Test: Try to get a connection from the pool.
            try (Connection connection = dataSource.getConnection()) {
                assertNotNull(connection, "GetConnection should return a valid connection.");
                assertTrue(connection.isValid(1), "The connection should be valid.");
            }

        } catch (Exception e) {
            // If any exception is thrown, the test fails.
            fail("An exception was thrown during the test: " + e.getMessage());
        } finally {
            // 3. Cleanup Test: Ensure the close method works without errors.
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }
}