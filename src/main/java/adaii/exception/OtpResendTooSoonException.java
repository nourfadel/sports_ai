package adaii.exception;

public class OtpResendTooSoonException extends RuntimeException {
    public OtpResendTooSoonException(String message) {
        super(message);
    }
}
