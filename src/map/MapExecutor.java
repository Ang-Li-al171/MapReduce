package map;

import keyvaluepair.KeyValuePair;
import network.NetworkMaster;

public class MapExecutor {
	private NetworkMaster myNetwork;
	
    public void registerNetwork(NetworkMaster networkMaster){
        myNetwork = networkMaster;
    }
	
	public void parseAndMap(String s) {
        int n = Math.abs(s.trim().hashCode() % myNetwork.getNodeListSize());
        myNetwork.sendKVPToNode(n, new KeyValuePair<String, Integer>(s, 1));
	}
}
