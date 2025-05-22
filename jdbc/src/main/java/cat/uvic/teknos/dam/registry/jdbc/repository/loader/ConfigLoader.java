package cat.uvic.teknos.dam.registry.jdbc.repository.loader;

import java.io.IOException;
import java.io.InputStream;

public interface ConfigLoader {
    DataSourceConfig load(InputStream in) throws IOException;
}
