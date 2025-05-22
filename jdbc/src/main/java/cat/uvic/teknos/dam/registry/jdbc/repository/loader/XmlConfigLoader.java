package cat.uvic.teknos.dam.registry.jdbc.repository.loader;

import java.io.IOException;
import java.io.InputStream;

public class XmlConfigLoader {
    private final JAXBContext context;

    public XmlConfigLoader() {
        try {
            // Inicializa el contexto JAXB con la clase de configuración
            context = JAXBContext.newInstance(DataSourceConfig.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Error inicializando JAXB", e);
        }
    }

    @Override
    public DataSourceConfig load(InputStream in) throws IOException {
        try {
            return (DataSourceConfig) context
                    .createUnmarshaller()
                    .unmarshal(in);
        } catch (JAXBException e) {
            throw new IOException("Error parseando XML", e);
        }
    }
}
