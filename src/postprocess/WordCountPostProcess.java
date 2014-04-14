package postprocess;

import network.NetworkMaster;
import keyvaluepair.KeyValuePair;

public class WordCountPostProcess implements PostProcess{
	
	NetworkMaster myNetwork;
	
	public WordCountPostProcess(NetworkMaster network) {
		myNetwork = network;
	}

	@Override
	public void receiveKVP(KeyValuePair<String, Integer> kvp) {
		System.out.println("RESULT: " + kvp.getKey() + " "
                + kvp.getValue() + ". Time spent: " + myNetwork.timeSpent(System.currentTimeMillis()));
		
	}
	
}
