package main;
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class NaiveWordCount {
    
    private HashMap<String, Integer> map;
    private static Scanner s;
    private static final String root = "src/files/";
    private static final Pattern PUNCTUATION = Pattern.compile("[\"(){},.;!?<>%]");
    
    public NaiveWordCount() {
        map = new HashMap<String, Integer>();
    }
    
    public void countWords(String file) throws FileNotFoundException {
        file = root+file;
        File fileName = new File(file);
        try {
            s = new Scanner(fileName);
            while (s.hasNext()) {
                String rawWord = s.next();
                String word = removePunctiation(rawWord);
                if (map.containsKey(word)) {
                    int count = map.get(word);
                    count++;
                    map.put(word, count);
                }
                else {
                    map.put(word, 1);
                }
            }
        } finally {
            if (s != null) {
                s.close();
            }
        }    
    }
    
    private static String removePunctiation(String x) {
        return PUNCTUATION.matcher(x).replaceAll("");
    }
    
    public void printWordCount() {
        for (String word: map.keySet()) {
            int count = map.get(word);
            System.out.println(word + " : "+ count);
        }
    }

}
