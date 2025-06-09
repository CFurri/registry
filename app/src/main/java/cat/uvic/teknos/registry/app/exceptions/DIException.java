package cat.uvic.teknos.registry.app.exceptions;

import java.io.IOException;

/**
 * A custom exception for the Dependency Injection manager.
 */
public class DIException extends RuntimeException {

    // Default constructor
    public DIException() {
        super();
    }

    // Constructor to handle simple error messages
    public DIException(String message) {
        super(message);
    }

    // This is the most important constructor. It accepts any kind of
    // root cause (IOException, ClassNotFoundException, etc.).
    public DIException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that wraps another exception
    public DIException(Throwable cause) {
        super(cause);
    }
}
