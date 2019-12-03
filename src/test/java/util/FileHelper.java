package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public class FileHelper {
    public static Scanner getScanner(String filename) throws FileNotFoundException {
        ClassLoader classLoader = FileHelper.class.getClassLoader();
        URL resourceUrl = classLoader.getResource(filename);
        assert resourceUrl != null;
        File file = new File(resourceUrl.getFile());
        return new Scanner(file);
    }
}
