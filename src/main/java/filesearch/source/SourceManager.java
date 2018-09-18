package filesearch.source;

public interface SourceManager<T> {

    boolean hasNext();

    Source<T> next();

    void closeAllSources();

}
