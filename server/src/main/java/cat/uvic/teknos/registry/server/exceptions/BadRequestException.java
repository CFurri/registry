package cat.uvic.teknos.registry.server.exceptions;
//Les excepcions personalitzades per enviar codis d'error al client
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
