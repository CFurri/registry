package cat.uvic.teknos.registry.server.exceptions;

public class ConflictException extends RuntimeException {
    public ConflictException(String message, RuntimeException e) {
        super(message);
    }
}
