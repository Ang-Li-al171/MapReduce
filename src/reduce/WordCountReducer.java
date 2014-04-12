package reduce;

import java.util.HashMap;
import java.util.HashSet;

import keyvaluepair.KeyValuePair;
import network.NetworkMaster;
import output.OutputCollector;

public class WordCountReducer implements Reducer<String, Integer> {

	private NetworkMaster myNetwork;
	private HashMap<String, Integer> list;
	private HashSet<Integer> reduceEOF;
	private int jobCounter;

    public WordCountReducer (NetworkMaster network) {
        myNetwork = network;
        list = new HashMap<String, Integer>();
        reduceEOF = new HashSet<Integer>();
        jobCounter = 0;
    }

	@Override
	public void reduce() {
		for (String s: list.keySet()) {
			KeyValuePair result = new KeyValuePair<String, Integer>(s, list.get(s));
			myNetwork.sendKVPToPortAndIP(myNetwork.getHostIP(), myNetwork.getHostPort(), result);
		}

		list = new HashMap<String, Integer>();
        reduceEOF = new HashSet<Integer>();
	}
	
	@Override
	public void addKVP(KeyValuePair kvp) {
		//jobCounter++;
		String key = kvp.getKey().toString();
		int value = (Integer) kvp.getValue();
		if(!list.containsKey(key)) {
			list.put(key, value);
		} else {
			int old = list.get(key);
			list.put(key, old + value);
		}
		//jobCounter--;
		//notifyAll();
	}
	
	@Override
	    public synchronized void incrementCounter() {
	    	jobCounter++;
	    	System.out.println("INCRE: CURRENT REDUCE COUNTER: " + jobCounter);
	}
	
	@Override
    public synchronized void decrementCounter() {
    	jobCounter--;
    	System.out.println("DECRE: CURRENT REDUCE COUNTER: " + jobCounter);
    	notifyAll();
    }
	
	@Override
	public synchronized void receiveEOF(int port) {
		reduceEOF.add(port);
		if (reduceEOF.size() == myNetwork.getNodeListSize()){
			while (jobCounter != 0) {
				try {
					wait();
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
			reduce();
			System.out.println("REDUCE END");
		}
		
	}
}