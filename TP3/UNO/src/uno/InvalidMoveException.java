package uno;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException() {
        super();
    }
    public InvalidMoveException(String message) {
        super(message);
    }
}