package cat.uvic.teknos.dam.jdbc.datasource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingleConnectionDataSourceTest {



    @Test
    void getConnectionQueNoFuncione() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource("mysql","localhost:3306","arnatte","userfalso","passwordfalso");
        assertThrows(RuntimeException.class, ds::getConnection);
    }
    @Test
    void getConnectionQueFuncione() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource("mysql","localhost:3306","mi_base","root","rootpassword");
        assertDoesNotThrow(ds::getConnection);
        assertNotNull(ds.getConnection());
    }

    @Test
    void getDriver() {
    }

    @Test
    void getServer() {
    }

    @Test
    void getDatabase() {
    }

    @Test
    void getUser() {
    }

    @Test
    void getPassword() {
    }
}
