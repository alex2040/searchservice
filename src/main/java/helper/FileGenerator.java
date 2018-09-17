package helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class FileGenerator {

    public static void main(String[] args) {

        String defaultPath = System.getProperty("java.io.tmpdir") + "/search";
        System.out.printf("Specify absolute path where files will be generated [default: %s]:", defaultPath);
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        if (path.equals("")) {
            path = defaultPath;
        }

        Integer count = null;
        while (count == null) {
            System.out.print("Specify files count [default: 20]:");
            String stringCount = scanner.nextLine();
            if (stringCount.equals("")) {
                count = 20;
            } else {
                try {
                    count = Integer.valueOf(stringCount);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        for (int i = 0; i < count; i++) {
            try {
                String fileName = path + "/file" + i;
                System.out.printf("Creating file %s \n", fileName);
                createFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("Finish");
    }

    private static void createFile(String file) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(new File(file)));
        Random random = new Random();
        for (int i = 0; i < 100_000_000; i++) {
            outputStream.write((String.valueOf(random.nextInt()) + ",").getBytes());
        }
        outputStream.close();
    }
}
