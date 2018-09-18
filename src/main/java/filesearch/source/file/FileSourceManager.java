package filesearch.source.file;

import filesearch.source.Source;
import filesearch.source.SourceException;
import filesearch.source.SourceManager;
import filesearch.source.SourceNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileSourceManager implements SourceManager<InputStream> {

    private final FileSourceIterator iterator;

    private List<Source<InputStream>> sourcesToClose;

    public FileSourceManager(String path) throws SourceException {
        sourcesToClose = new ArrayList<>();
        Path sourcePath = Paths.get(path);
        if (!sourcePath.toFile().exists()) {
            throw new SourceException(String.format("path does not exist: %s", path));
        }
        OriginFileSourceIterator originIterator;
        try {
            FileSourceSorter.INSTANCE.sort(path);
            originIterator = new OriginFileSourceIterator(sourcePath);
            iterator = new FileSourceIterator(sourcePath);
        } catch (IOException e) {
            throw new SourceException(e.getMessage(), e);
        }
        if (!originIterator.hasNext()) {
            throw new SourceNotFoundException(String.format("no source found at %s", path));
        }
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Source<InputStream> next() {
        Source<InputStream> source = iterator.next();
        sourcesToClose.add(source);
        return source;
    }

    @Override
    public void closeAllSources() {
        sourcesToClose.forEach(Source::close);
    }
}
