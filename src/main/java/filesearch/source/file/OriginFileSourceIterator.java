package filesearch.source.file;

import filesearch.source.Source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

class OriginFileSourceIterator {

    private Iterator<Path> iterator;

    OriginFileSourceIterator(Path path) throws IOException {
        iterator = Files.newDirectoryStream(
                path, entry -> {
                    File file = entry.toFile();
                    return file.isFile() && !file.getName().endsWith(".sorted") && !file.getName().endsWith(".properties");
                }
        ).iterator();
    }

    boolean hasNext() {
        if (iterator == null) {
            return false;
        }
        return iterator.hasNext();
    }

    Source<InputStream> next() {
        if (iterator == null) {
            return null;
        }
        Path path = iterator.next();
        return new FileSource(path.toFile());
    }
}
