package output;
import java.util.*;

import map.Mapper;
import network.NetworkMaster;

import keyvaluepair.KeyValuePair;
/**
Receives KVPs from mappers/reducers, and pass the results to the
corresponding machines determined by mappers/reducers.
*/
public class Distributor<K, V> {

    private NetworkMaster<K,V> myNetwork;
    private Mapper mapper;
	
    public Distributor (NetworkMaster network) {
        myNetwork = network;
    }
	
	/**
	 * Called after map, shuffles k,v pairs to new nodes
	 * @param kvp
	 */
	public void collectAndShuffle(K key, V value) {
		KeyValuePair<K, V> kvp = new KeyValuePair<K, V>(key, value);
	    //shuffler.assignToMachine(kvp);
		
		String word = kvp.getKey().toString();
        int n = Math.abs(word.trim().hashCode() % myNetwork.getNodeListSize());
        mapper.incrementSentCounts(n);
        myNetwork.sendKVPToNode(n, kvp);
	}
	
	/**
	 * Sends KVP to host
	 * @param kvp
	 */
	public void collectAndSendToHost(K key, V value) {
		KeyValuePair<K, V> kvp = new KeyValuePair<K, V>(key, value);
		//shuffler.assignToHost(kvp);
		
		myNetwork.sendKVPToPortAndIP(myNetwork.getHostIP(), myNetwork.getHostPort(), kvp);
	}
	
    public void setMapper(Mapper m) {
        mapper = m;      
    }

}