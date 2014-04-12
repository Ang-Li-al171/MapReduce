package input;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * This class reads input files and parses them.
 * @author weideng
 *
 */
public class FileReader {
    
    private static final Pattern PUNCTUATION = Pattern.compile("[(){},.;!?<>%]\"");
    private Splitter splitter;
    private Scanner s;
    private final String root = "src/files/";
    
    public FileReader(Splitter s) {
        splitter = s;
    }
    
    public void read(String file) throws FileNotFoundException {    
        file = root+file;
        File fileName = new File(file);
        try {
            s = new Scanner(fileName);
            while (s.hasNext()) {
                String line = s.nextLine();
                //String filteredWord = removePunctiation(rawWord);
                splitter.assignToNode(line);
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }         
    }
    
    public int[] getCounts(){
        return splitter.getMapCounts();
    }
    private static String removePunctiation(String x) {
        return PUNCTUATION.matcher(x).replaceAll("");
    }
}
