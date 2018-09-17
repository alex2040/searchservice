package filesearch.engine.file;

import filesearch.engine.SearchEngine;
import filesearch.engine.Searcher;
import filesearch.source.Source;
import filesearch.source.SourceException;
import filesearch.source.SourceManager;
import filesearch.source.file.FileSourceManager;

import java.io.InputStream;

public class FileSearchEngine extends SearchEngine<InputStream> {

    private String path;

    public FileSearchEngine(String path) {
        this.path = path;
    }

    @Override
    protected SourceManager<InputStream> createSourceManager() throws SourceException {
        return new FileSourceManager(path);
    }

    @Override
    protected Searcher createSearcher(Source<InputStream> source) {
        return new FileSearcher(source);
    }
}
