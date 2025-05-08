package cat.uvic.teknos.m0846.jdbc;

import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcApp {
    public static void main(String[] args) {
        try {
            executeLogic();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void executeLogic() throws SQLException{
        try(var connection = DriverManager.getConnection("jdbc:mysql://localhost/3306/registry", "root", "rootpassword"))

        System.out.println("Schema: " + connection);

        var statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO PERSON(ID, FIRST_NAME, LAST_NAME) VALUES (1, 'John', 'Smith')");

        addPerson(connection, 2, "David", "Gilmour");
        addPerson(connection, 3, "Jimi", "Hendriz");
        addPerson(connection, 4, "John", "Parker");

        var statement = connection.createStatement();
        var results = statement.executeQuery("select * from PERSON");
        while (results.next()){
            System.out.println("ID: " + results.getInt("ID: "));
            System.out.println("FIRST_NAME: " + results.getString("FIRST_NAME"));
            System.out.println("LAST_NAME "+ results.getString("LAST_NAME"));
        }
    }


    private static void addPerson(Connection connection, int id, int String firstName, String lastName) throws SQLException{
        var statement = connection.createStatement("insert into Person (id, firstName, lastName) value (?,?,?)");
        preparedStatement.setInt(1, id);

        statement.executeUpdate("INSERT INTO PERSON(ID, FIRST_NAME, LAST_NAME) VALUES (1, 'John', 'Smith')");
    }
}



