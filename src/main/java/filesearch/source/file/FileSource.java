package filesearch.source.file;

import filesearch.source.Source;
import filesearch.source.SourceException;

import java.io.*;

public class FileSource implements Source<InputStream> {

    private File file;
    private InputStream inputStream;

    FileSource(File file) {
        this.file = file;
    }

    @Override
    public String getName() {
        return file.getAbsolutePath();
    }

    @Override
    public InputStream getSource() throws SourceException {
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new SourceException(e.getMessage(), e);
        }
        return inputStream;
    }

    @Override
    public void close() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
