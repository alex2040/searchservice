package filesearch.source;

public class SourceNotFoundException extends SourceException {
    public SourceNotFoundException(String message) {
        super(message);
    }

    public SourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
