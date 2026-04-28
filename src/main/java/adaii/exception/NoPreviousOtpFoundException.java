package adaii.exception;

public class NoPreviousOtpFoundException extends RuntimeException {
    public NoPreviousOtpFoundException(String message) {
        super(message);
    }
}
