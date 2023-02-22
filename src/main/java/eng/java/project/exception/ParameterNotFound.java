package eng.java.project.exception;

public class ParameterNotFound extends Exception {
    public ParameterNotFound() {
        super("Parameter of prepared statement not found");
    }

    public ParameterNotFound(String message) {
        super(message);
    }

    public ParameterNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterNotFound(Throwable cause) {
        super(cause);
    }

    public ParameterNotFound(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
