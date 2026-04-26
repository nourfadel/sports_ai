package ADAII.exception;

public class PlayerProfileNotFoundException extends RuntimeException {
    public PlayerProfileNotFoundException(String message) {
        super(message);
    }
}
