package eng.java.project.exception;

public class UnknownEnumerationValue extends Exception{
    public UnknownEnumerationValue() {
        super("Unknown enumeration value received");
    }

    public UnknownEnumerationValue(String message) {
        super(message);
    }

    public UnknownEnumerationValue(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownEnumerationValue(Throwable cause) {
        super(cause);
    }

    public UnknownEnumerationValue(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
