package reduce;

import java.util.*;
import keyvaluepair.KeyValuePair;
import network.NetworkMaster;


public class WordCountReducer implements Reducer<String, Integer> {

    private NetworkMaster myNetwork;
    private Map<String, List<Integer>> mapList;
    private Map<String, Iterator<Integer>> mapIterator;
    private HashSet<Integer> reduceEOF;
    private int jobCounter;

    public WordCountReducer (NetworkMaster network) {
        myNetwork = network;
        mapList = new HashMap<>();
        mapIterator = new HashMap<>();
        reduceEOF = new HashSet<Integer>();
        jobCounter = 0;
    }

    @Override
    public void reduce (String key, Iterator<Integer> values) {
        int sum = 0;
        while (values.hasNext()) {
            sum += values.next();
        }
        KeyValuePair<String, Integer> result = new KeyValuePair<String, Integer>(key, sum);
        myNetwork.sendKVPToPortAndIP(myNetwork.getHostIP(), myNetwork.getHostPort(), result);
        // list = new HashMap<String, Integer>();
        // reduceEOF = new HashSet<Integer>();
    }

    @Override
    public synchronized void addKVP (KeyValuePair<String, Integer> kvp) {
        String key = kvp.getKey();
        int value = kvp.getValue();
        if (!mapList.containsKey(key)) {
            List<Integer> counts = new ArrayList<>();
            counts.add(value);
            mapList.put(key, counts);
        }
        else {
            mapList.get(key).add(value);
        }
    }

    public void convertListToIterator () {
        for (String word : mapList.keySet()) {
            List<Integer> counts = mapList.get(word);
            Iterator<Integer> iterator = counts.iterator();
            mapIterator.put(word, iterator);
        }
    }

    @Override
    public synchronized void incrementCounter () {
        jobCounter++;
        System.out.println("INCRE: CURRENT REDUCE COUNTER: " + jobCounter);
    }

    @Override
    public synchronized void decrementCounter () {
        jobCounter--;
        System.out.println("DECRE: CURRENT REDUCE COUNTER: " + jobCounter);
        notifyAll();
    }

    @Override
    public synchronized void receiveEOF (int port) {
        reduceEOF.add(port);
        if (reduceEOF.size() == myNetwork.getNodeListSize()) {
            while (jobCounter != 0) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                     e.printStackTrace();
                }
            }
            convertListToIterator();
            for (String word : mapIterator.keySet()) {
                reduce(word, mapIterator.get(word));
            }
            System.out.println("REDUCE DONE");
        }

    }
}
