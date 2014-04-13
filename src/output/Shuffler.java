package output;

import java.util.List;
import keyvaluepair.KeyValuePair;
import map.Mapper;
import network.NetworkMaster;

/**
 * Redistributes K,V pairs after Map phase, before Reduce phase
 * @author weideng
 *
 */
public class Shuffler<K,V> {
    
    private NetworkMaster myNetwork;
    private List<KeyValuePair<K,V>> tuples;
    private Mapper mapper;

    public Shuffler(NetworkMaster network) {
        myNetwork = network;
    }
    
    /**
     * Shuffles keys to nodes based on hashcode
     * @param KVP
     */
    public void assignToMachine(KeyValuePair<K,V> KVP) {
        String word = KVP.getKey().toString();
        int n = Math.abs(word.trim().hashCode() % myNetwork.getNodeListSize());
        mapper.incrementSentCounts(n);
        myNetwork.sendKVPToNode(n, new KeyValuePair<String, Integer>(word, 1));      
    }
    
    /**
     * Sends finalized keys to host node
     * @param KVP
     */
    public void assignToHost(KeyValuePair<String, Integer> KVP) {
        myNetwork.sendKVPToPortAndIP(myNetwork.getHostIP(), myNetwork.getHostPort(), KVP);
    }
    
}
