package filesearch.engine.file;

import filesearch.engine.SearchException;
import filesearch.engine.Searcher;
import filesearch.source.Source;
import filesearch.source.SourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileSearcher implements Searcher {

    private final Source<InputStream> source;

    private final Logger logger;

    FileSearcher(Source<InputStream> source) {
        this.source = source;
        logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    public boolean search(int number) throws SearchException {
        try {
            int intCount = source.getSource().available() / 4;
            if (intCount == 0) {
                logger.error("[" + source.getName() + ".sorted] is empty");
                return false;
            }
            return search(number, 0, intCount);
        } catch (IOException e) {
            throw new SearchException(e.getMessage(), e);
        }
    }

    private boolean search(int number, int left, int right) throws SourceException {
        if (right - left == 1) {
            if (readDataInput(left) == number) {
                return true;
            }
            return readDataInput(right) == number;
        }
        int mid = (left + right) / 2;
        int readInt = readDataInput(mid);
        if (readInt == number) {
            return true;
        }
        if (number < readInt) {
            return search(number, left, mid);
        } else {
            return search(number, mid, right);
        }
    }

    private int readDataInput(int offset) throws SourceException {
        DataInputStream dataInputStream = getDataInputStream();
        try {
            dataInputStream.skipBytes(offset * 4);
            return dataInputStream.readInt();
        } catch (IOException e) {
            throw new SourceException(e.getMessage(), e);
        }
    }

    private DataInputStream getDataInputStream() throws SourceException {
        DataInputStream dataInputStream = new DataInputStream(new BufferedInputStream(source.getSource()));
        dataInputStream.mark(0);
        return dataInputStream;
    }
}
