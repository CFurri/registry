package cat.uvic.teknos.registry.jdbc.datasource;

import cat.uvic.teknos.registry.jdbc.exceptions.DataSourceException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariDataSourceAdapter implements DataSource {
    private final HikariDataSource dataSource;

    public HikariDataSourceAdapter() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getResourceAsStream("/datasource.properties")) {
            if (input == null) {
                throw new RuntimeException("Fitxer datasource.properties no trobat");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error carregant datasource.properties", e);
        }

        HikariConfig config = new HikariConfig();

        // *** START OF FIX ***
        String driver = properties.getProperty("driver");
        String jdbcUrl;

        if ("h2".equalsIgnoreCase(driver)) {
            // H2 in-memory URL has a different format
            jdbcUrl = String.format("jdbc:%s:%s",
                driver,
                properties.getProperty("server"));
        } else {
            // Original logic for server-based databases like MySQL
            jdbcUrl = String.format("jdbc:%s://%s/%s",
                driver,
                properties.getProperty("server"),
                properties.getProperty("database"));
        }
        // *** END OF FIX ***

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(properties.getProperty("user"));
        config.setPassword(properties.getProperty("password"));
        config.setDriverClassName(getDriverClass(driver));

        // Optional: additional pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);

        this.dataSource = new HikariDataSource(config);
    }

    @Override
    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataSourceException("Error obtenint connexió del pool Hikari", e);
        }
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    private String getDriverClass(String driverAlias) {
        return switch (driverAlias.toLowerCase()) {
            case "mysql" -> "com.mysql.cj.jdbc.Driver";
            case "postgresql" -> "org.postgresql.Driver";
            case "h2" -> "org.h2.Driver";
            default -> throw new IllegalArgumentException("Driver desconegut: " + driverAlias);
        };
    }
}