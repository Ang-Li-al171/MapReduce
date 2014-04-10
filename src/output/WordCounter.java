package output;

import java.util.HashMap;
import java.util.Map;

public class WordCounter {
    
    private static WordCounter myCounter = new WordCounter();
    private Map<String, Integer> wc;
    
    public WordCounter getInstance(){
        return myCounter;
    }
    private WordCounter(){
        wc = new HashMap<String, Integer>();
    }
    
    public Map<String, Integer> getResults(){
        return wc;
    }
    
    public void add(String toAdd){
        if (wc.containsKey(toAdd)){
            wc.put(toAdd, wc.get(toAdd) + 1);
        } else{
            wc.put(toAdd, 1);
        }
    }
}
