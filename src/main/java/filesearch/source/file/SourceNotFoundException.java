package filesearch.source.file;

import filesearch.source.SourceException;

class SourceNotFoundException extends SourceException {
    SourceNotFoundException(String message) {
        super(message);
    }
}
