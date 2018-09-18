package filesearch.source.file;

import filesearch.source.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public enum FileSourceSorter {

    INSTANCE;

    private static final int BLOCK_SIZE = 102_428_800;

    private static final byte COMMA_ASCII_CODE = 44;

    private static final String FILES_PROPERTIES_FILENAME = "files.properties";

    private final Logger logger;

    private volatile boolean inProgress;

    FileSourceSorter() {
        logger = LoggerFactory.getLogger(this.getClass());
    }

    public void sort(String path) throws IOException {
        if (inProgress) {
            logger.debug("sorting is already in progress. exit");
            return;
        }
        inProgress = true;
        OriginFileSourceIterator originFileSourceIterator = new OriginFileSourceIterator(Paths.get(path));
        Properties filesProperties = getFilesProperties(path);
        while (originFileSourceIterator.hasNext()) {
            Source<InputStream> originSource = originFileSourceIterator.next();
            if (checkIfSortNeeded(originSource, filesProperties)) {
                sortFile(originSource);
                updateFilesProperties(originSource, path);
            }
        }
        inProgress = false;
    }

    private Properties getFilesProperties(String path) throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(path + File.separator + FILES_PROPERTIES_FILENAME)) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException ignored) {
        }
        return properties;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean checkIfSortNeeded(Source<InputStream> originSource, Properties filesProperties) {
        File sortedFile = new File(originSource.getName() + ".sorted");
        if (!sortedFile.exists()) {
            return true;
        }
        String lastModifyTime = filesProperties.getProperty(originSource.getName());
        if (lastModifyTime == null) {
            return true;
        }
        if (new File(originSource.getName()).lastModified() != Long.valueOf(lastModifyTime)) {
            return true;
        }
        return false;
    }

    private void sortFile(Source<InputStream> source) throws IOException {
        logger.debug("Sorting of [" + source.getName() + "] started");
        long startTime = System.currentTimeMillis();
        BufferedInputStream inputStream = new BufferedInputStream(source.getSource());
        int blockCount = getBlockCount(inputStream);

        int i = 0;
        int[] integers = new int[getNumberCount(source)];
        for (int block = 1; block <= blockCount; block++) {
            byte[] bytes = new byte[BLOCK_SIZE + 12];
            int readBytesCount = readNextBytesChunk(inputStream, bytes);
            if (readBytesCount == -1) {
                break;
            }
            Scanner scanner = new Scanner(new ByteArrayInputStream(bytes, 0, readBytesCount)).useDelimiter(",");

            while (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                integers[i++] = number;
            }
        }

        FileOutputStream fileOutputStream = new FileOutputStream(source.getName() + ".sorted");
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));
        integers = Arrays.stream(integers).parallel().sorted().toArray();

        for (Integer integer : integers) {
            dataOutputStream.writeInt(integer);
        }
        dataOutputStream.flush();
        dataOutputStream.close();
        long endTime = System.currentTimeMillis();
        logger.debug("Sorting of [" + source.getName() + "] took: " + (endTime - startTime) / 1000 + " seconds");
    }

    private void updateFilesProperties(Source<InputStream> source, String path) throws IOException {
        Properties filesProperties = getFilesProperties(path);
        String fileName = source.getName();
        filesProperties.setProperty(fileName, String.valueOf(new File(fileName).lastModified()));
        filesProperties.store(new FileOutputStream(path + FILES_PROPERTIES_FILENAME), null);
    }

    private int getNumberCount(Source<InputStream> source) throws IOException {
        int count = 0;
        BufferedInputStream inputStream = new BufferedInputStream(source.getSource());
        while (inputStream.available() > 0) {
            byte[] bytes = new byte[BLOCK_SIZE];
            int read = inputStream.read(bytes);
            for (int i = 0; i < read; i++) {
                if (bytes[i] == 44) {
                    count++;
                }
            }
        }
        return count;
    }

    private int getBlockCount(InputStream inputStream) throws IOException {
        int fileSize = inputStream.available();
        int blockCount = fileSize / BLOCK_SIZE;
        if (fileSize % BLOCK_SIZE > 0) {
            blockCount++;
        }
        return blockCount;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private int readNextBytesChunk(InputStream inputStream, byte[] bytes) throws IOException {
        int readBytesCount = inputStream.read(bytes, 0, BLOCK_SIZE);
        if (readBytesCount == -1) {
            return -1;
        }
        while (bytes[readBytesCount - 1] != COMMA_ASCII_CODE && inputStream.available() > 0) {
            inputStream.read(bytes, readBytesCount++, 1);
        }
        return readBytesCount;
    }
}
