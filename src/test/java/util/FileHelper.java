package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class FileHelper {
    public static Scanner getScanner(String filename) {
        ClassLoader classLoader = FileHelper.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(filename);
        assert resourceUrl != null;
        File file = new File(resourceUrl.getFile());
        try {
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not read file " + filename, e);
        }
    }

    public static int[] readProgramInput(String filename) {
        Scanner scanner = getScanner(filename);
        String[] inputAsStrings = scanner.nextLine().split(",");
        int[] input = new int[inputAsStrings.length];
        for (int i = 0; i < inputAsStrings.length; i++) {
            input[i] = Integer.parseInt(inputAsStrings[i]);
        }
        return input;
    }
}
