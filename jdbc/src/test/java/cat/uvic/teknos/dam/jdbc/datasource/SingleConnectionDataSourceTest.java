package cat.uvic.teknos.dam.jdbc.datasource;

import cat.uvic.teknos.dam.registry.jdbc.repository.datasource.SingleConnectionDataSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SingleConnectionDataSourceTest {



    @Test
    void getConnectionQueNoFuncione() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource("mysql","localhost:3306","registry","root","rootpassword");
        assertThrows(RuntimeException.class, ds::getConnection);
    }
    @Test
    void getConnectionQueFuncione() {
        SingleConnectionDataSource ds = new SingleConnectionDataSource("mysql","localhost:3306","registry","root","rootpassword");
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
