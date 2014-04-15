package reduce;

import java.util.*;
import output.Distributor;
import keyvaluepair.KeyValuePair;
import network.NetworkMaster;


public class WordCountReducer implements Reducer<String, Integer> {

    private NetworkMaster myNetwork;
    private Map<String, List<Integer>> mapList;
    private Map<String, Iterator<Integer>> mapIterator;
    private HashSet<Integer> reduceEOF;
    private int jobDone;
    private int totalRevCount;

    public WordCountReducer (NetworkMaster network) {
        myNetwork = network;
        mapList = new HashMap<String, List<Integer>>();
        mapIterator = new HashMap<String, Iterator<Integer>>();
        reduceEOF = new HashSet<Integer>();
        jobDone = 0;
        totalRevCount = 0;
    }

    public void reduce (String key, Iterator<Integer> values, Distributor<String,Integer> o) {
        int sum = 0;
        while (values.hasNext()) {
            sum += values.next();
        }
        o.collectAndSendToHost(key, sum);
    }

    @Override
    public synchronized void addKVP (KeyValuePair<String, Integer> kvp) {
        String key = kvp.getKey();
        int value = kvp.getValue();
        if (!mapList.containsKey(key)) {
            List<Integer> counts = new ArrayList<Integer>();
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
    public synchronized void jobDoneCount() {
        jobDone++;
        System.out.println("Increment: JOBDONE COUNT: " + jobDone);
        notifyAll();
    }

    @Override
    public synchronized void receiveEOF (int port, int count) {
        
        reduceEOF.add(port);
        totalRevCount +=count;
        
        if (reduceEOF.size() == myNetwork.getNodeListSize()) {
            while (jobDone < totalRevCount) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                     e.printStackTrace();
                }
            }
            convertListToIterator();
            //Shuffler shuffler = new Shuffler(myNetwork);
            Distributor output = new Distributor(myNetwork);
            for (String word : mapIterator.keySet()) {
                reduce(word, mapIterator.get(word), output);
            }
            System.out.println("REDUCE DONE");
        }
    }
}
