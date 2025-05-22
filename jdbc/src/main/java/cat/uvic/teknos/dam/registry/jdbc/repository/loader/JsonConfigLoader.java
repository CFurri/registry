package cat.uvic.teknos.dam.registry.jdbc.repository.loader;

import org.gradle.internal.impldep.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonConfigLoader {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public DataSourceConfig load(InputStream in) throws IOException {
        return mapper.readValue(in, DataSourceConfig.class);
    }
}

