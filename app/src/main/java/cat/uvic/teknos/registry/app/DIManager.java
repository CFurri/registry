package cat.uvic.teknos.registry.app;

import cat.uvic.teknos.registry.app.exceptions.DIException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A simple Dependency Injection Manager.
 * It reads class implementations from a properties file and manages them as singletons.
 */
public class DIManager {
    private static final String PROPERTIES_FILE = "/di.properties";
    private final Properties properties;

    // Cache to store singleton instances, ensuring they are only created once.
    private final Map<String, Object> instances = new HashMap<>();

    public DIManager() throws DIException {
        properties = new Properties();
        try (InputStream input = DIManager.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new DIException("Resource file not found: " + PROPERTIES_FILE);
            }
            properties.load(input);
        } catch (IOException e) {
            throw new DIException("Failed to load properties file: " + PROPERTIES_FILE, e);
        }
    }
        // Afegit mètode put() per fer funcionar app:server <---
    /**
     * Manually registers an already created instance as a singleton.
     * This is useful for objects that require complex construction.
     *
     * @param key The key to register the instance under.
     * @param instance The object instance to register.
     */
    public void put(String key, Object instance) {
        instances.put(key, instance);
    }

    /**
     * Gets a shared singleton instance of the class specified by the property key.
     * The instance is created on the first call and cached for subsequent calls.
     *
     * @param key The key in the di.properties file (e.g., "repository_factory").
     * @param <T> The type of the instance to return.
     * @return The singleton instance.
     * @throws DIException if the class cannot be found or instantiated.
     */
    public <T> T get(String key) throws DIException {
        // Check if we already have an instance of this singleton
        if (instances.containsKey(key)) {
            return (T) instances.get(key);
        }

        // If not, create a new one
        try {
            String className = properties.getProperty(key);
            if (className == null || className.trim().isEmpty()) {
                throw new DIException("Property key not found in " + PROPERTIES_FILE + ": " + key);
            }

            // Get the class and create a new instance using its default constructor
            Class<?> clazz = Class.forName(className);
            T instance = (T) clazz.getConstructor().newInstance();

            // Cache the new instance for future calls
            instances.put(key, instance);

            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Provide a more detailed error message
            throw new DIException("Failed to instantiate class for key '" + key + "': " + properties.getProperty(key), e);
        }
    }

    /**
     * Attempts to gracefully close any managed singletons that have a "close()" method.
     * This is useful for releasing resources like database connection pools.
     */
    public void close() {
        for (Object instance : instances.values()) {
            try {
                Method closeMethod = instance.getClass().getMethod("close");
                closeMethod.invoke(instance);
            } catch (NoSuchMethodException e) {
                // It's okay if the instance doesn't have a close() method.
            } catch (Exception e) {
                // Log or print the error if closing fails for some reason.
                System.err.println("Error while closing instance of " + instance.getClass().getName() + ": " + e.getMessage());
            }
        }
        instances.clear();
    }
}
