package cat.uvic.teknos.registry.server.exceptions; // Assegura't que el paquet sigui el correcte

// No cal importar res si estàs al mateix paquet

public class BadRequestException extends RuntimeException {

    // Constructor que només accepta un missatge
    public BadRequestException(String message) {
        super(message);
    }

    // Constructor que accepta un missatge i la causa original de l'error
    // CANVI 1: Canviem IOException per Throwable per acceptar QUALSEVOL excepció
    public BadRequestException(String message, Throwable cause) {
        // CANVI 2: Passem la "causa" al constructor de la superclasse
        // Això preserva tota la informació de l'error original
        super(message, cause);
    }
}