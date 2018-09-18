package filesearch.engine;

import filesearch.source.SourceException;

import java.util.List;

public interface SearchEngine {

    List<SearchResult> search(int number) throws SourceException, SearchException;

}
