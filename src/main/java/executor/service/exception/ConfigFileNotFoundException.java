package executor.service.exception;

public class ConfigFileNotFoundException extends RuntimeException {
    public ConfigFileNotFoundException() {
    }

    public ConfigFileNotFoundException(String message) {
        super(message);
    }

    public ConfigFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
