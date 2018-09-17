package filesearch.source;

import java.io.Closeable;

public interface Source<T> extends Closeable {

    String getName();

    T getSource() throws SourceException;

    void close();
}
