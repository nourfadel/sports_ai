package adaii.exception;

public class PlayerProfileAlreadyExistException extends RuntimeException {
    public PlayerProfileAlreadyExistException(String message) {
        super(message);
    }
}
