package adaii.exception;

public class UnauthorizedSessionAccessException extends RuntimeException {
    public UnauthorizedSessionAccessException(String message) {
        super(message);
    }
}
