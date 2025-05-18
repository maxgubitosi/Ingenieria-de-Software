package uno;

/**
 * Excepción lanzada cuando un jugador intenta una jugada inválida.
 */
public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException() {
        super();
    }
    public InvalidMoveException(String message) {
        super(message);
    }
}