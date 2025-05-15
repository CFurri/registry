package cat.uvic.teknos.dam.registry.jdbc.repository.exceptions;

public class CrudException extends RuntimeException {
    public CrudException(String message) {
        super(message);
    }
}
