package cat.uvic.teknos.registry.jpa.exceptions;

public class EntityManagerException extends RuntimeException {

    public EntityManagerException(String message) {
        super(message);
    }

    public EntityManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityManagerException(Throwable cause) {
        super(cause);
    }

    public EntityManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
