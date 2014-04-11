package reduce;

import java.util.HashMap;
import java.util.HashSet;

import keyvaluepair.KeyValuePair;
import network.NetworkMaster;
import output.OutputCollector;

public class WordCountReducer implements Reducer<String, Integer> {

	private NetworkMaster myNetwork;
	private HashMap<String, Integer> list;
	private HashSet<Integer> mapEOF;

    public WordCountReducer (NetworkMaster network) {
        myNetwork = network;
        list = new HashMap<String, Integer>();
        mapEOF = new HashSet<Integer>();
    }

	@Override
	public void reduce() {
		for (String s: list.keySet()) {
			KeyValuePair result = new KeyValuePair<String, Integer>(s, list.get(s));
			myNetwork.sendKVPToPortAndIP(myNetwork.getHostIP(), myNetwork.getHostPort(), result);
		}

		list = new HashMap<String, Integer>();
        mapEOF = new HashSet<Integer>();
	}
	
	@Override
	public void addKVP(KeyValuePair kvp) {
		String key = kvp.getKey().toString();
		int value = (Integer) kvp.getValue();
		if(!list.containsKey(key)) {
			list.put(key, value);
		} else {
			int old = list.get(key);
			list.put(key, old + value);
		}
	}
	
	@Override
	public void receiveEOF(int port) {
		mapEOF.add(port);
		if (mapEOF.size() == myNetwork.getNodeListSize() - 1){
			reduce();
		}
	}
}