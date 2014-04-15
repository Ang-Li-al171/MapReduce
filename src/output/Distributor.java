package output;
import java.util.*;

import map.Mapper;
import network.NetworkMaster;

import keyvaluepair.KeyValuePair;
/***
 * Receives KVPs from mappers/reducers, and pass the results to the
 * corresponding machines determined by mappers/reducers.
 * @author weideng
 * @modified Wenshun Liu
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
		
		myNetwork.sendKVPToPortAndIP(myNetwork.getHostIP(), myNetwork.getHostPort(), kvp);
	}

	/**
	 * Sends KVP to machine of given index
	 * @param kvp
	 */
	public void collectAndSendToIndex(int index, K key, V value) {
		KeyValuePair<K, V> kvp = new KeyValuePair<K, V>(key, value);
		
		myNetwork.sendKVPToNode(index, kvp);
	}
	
    public void setMapper(Mapper m) {
        mapper = m;      
    }

}