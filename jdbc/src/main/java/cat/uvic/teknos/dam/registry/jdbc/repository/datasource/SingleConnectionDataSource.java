package cat.uvic.teknos.dam.registry.jdbc.repository.datasource;

import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.DataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SingleConnectionDataSource implements DataSource {
    private final DataSource dataSource;

    private Connection connection;
    private final String driver;
    private final String server;
    private final String database;
    private final String user;
    private final String password;

    public String getDriver() {
        return driver;
    }

    public String getServer() {
        return server;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public SingleConnectionDataSource(){
        var properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("datasource.properties"));
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        driver = properties.getProperty("driver");
        server = properties.getProperty("server");
        database = properties.getProperty("database");
        user = properties.getProperty("user");
        password = properties.getProperty("password");
    }

    @Override
    public Connection getConnection(){
        if(connection == null) {
            try {
                connection = DriverManager.getConnection(String.format("jdbc:%s://%s/%s", driver, server, database),
                        user,
                        password);

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}