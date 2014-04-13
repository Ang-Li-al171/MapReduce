package input;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class reads input files and parses them.
 * @author weideng
 *
 */
public class FileReader {

    private List<String> results;
    private Scanner s;
    private final String root = "src/files/";
    
    public FileReader() {
        results = new ArrayList<String>();
    }
    
    public List<String> readAndSplitByLine(String file) throws FileNotFoundException {    
        file = root+file;
        File fileName = new File(file);
        try {
            s = new Scanner(fileName);
            while (s.hasNext()) {
                String line = s.nextLine();
                results.add(line);
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }      
        return results;
    }
}
