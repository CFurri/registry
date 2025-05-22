package cat.uvic.teknos.dam.registry.jdbc.repository.loader;

import java.io.IOException;
import java.io.InputStream;

public class YamlConfigLoader {
    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @Override
    public DataSourceConfig load(InputStream in) throws IOException {
        return mapper.readValue(in, DataSourceConfig.class);
    }
}
