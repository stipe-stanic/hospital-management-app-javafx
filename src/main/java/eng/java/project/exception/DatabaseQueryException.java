package eng.java.project.exception;

public class DatabaseQueryException extends Exception {
    public DatabaseQueryException() {
        super("Error occurred while querying database");
    }

    public DatabaseQueryException(String message) {
        super(message);
    }

    public DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseQueryException(Throwable cause) {
        super(cause);
    }

    public DatabaseQueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
