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

    public Shuffler(NetworkMaster network, OutputCollector<K,V> o) {
        myNetwork = network;
        tuples = o.getTuples();
    }
    
    public void assignToMachine(KeyValuePair<K,V> KVP) {
        String word = KVP.getKey().toString();
        int n = Math.abs(word.trim().hashCode() % myNetwork.getNodeListSize());
        mapper.incrementSentCounts(n);
        myNetwork.sendKVPToNode(n, new KeyValuePair<String, Integer>(word, 1));      
    }
    
    public void shuffle() {
        for (KeyValuePair<K,V> kvp: tuples) {
            assignToMachine(kvp);
        }
    }
    
    public void setMapper(Mapper m) { //Assigns a mapper to this shuffler
        mapper = m;      
    }
    


}
