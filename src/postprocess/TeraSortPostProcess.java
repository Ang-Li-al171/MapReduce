package postprocess;

import java.util.HashMap;

import network.NetworkMaster;
import keyvaluepair.KeyValuePair;

public class TeraSortPostProcess implements PostProcess{

	private NetworkMaster myNetwork;
	private HashMap<Integer, String> results = new HashMap<Integer, String>();
	private int jobDone;
    private int totalRevCount;
	
	
	public TeraSortPostProcess(NetworkMaster network) {
		myNetwork = network;
		jobDone = 0;
        totalRevCount = 0;
	}

	@Override
	public synchronized void receiveKVP(KeyValuePair<String, Integer> kvp) {
		//System.out.println("RESULT: " + kvp.getKey());
		results.put(kvp.getValue(), kvp.getKey());
		jobDone++;
		if (jobDone == myNetwork.getNodeListSize()) {
			printResult();
		}
	}

	public void printResult() {
		for (int i = 0; i< myNetwork.getNodeListSize(); i++) {
			if (results.containsKey(i)){
				String[] temp = results.get(i).split(" ");
				for (String s: temp) System.out.println("RESULT: " + s);
			}
		}
	}
}
