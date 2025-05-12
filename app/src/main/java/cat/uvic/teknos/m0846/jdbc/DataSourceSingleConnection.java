package cat.uvic.teknos.m0846.jdbc;

public class DataSourceSingleConnection implements DataSource {
    @Override
    public Connection getConnection() {
        if (connection == null){
            try{
                connection = DriverManager.getConnection("jdbc:mysql://localhost:8080/registry", "root", "rootpassword");

            } catch (SQLException e){
                throw new RuntimeException;
            }
        }
        return connection;
    }
    private Connection connection;
}

