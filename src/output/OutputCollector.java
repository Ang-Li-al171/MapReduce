package output;
import java.util.*;

import keyvaluepair.KeyValuePair;
/**
Collects the <key,value> tuples created by map.
For multi-node jobs, networking calls will be made from
here to send tuples to other nodes.
*/
public class OutputCollector<K, V> {

	private List<KeyValuePair<K, V>> tuples; //Programmer should not be able to access this
	private Shuffler shuffler;
	private String phase;

	public OutputCollector(Shuffler s, String p) {
		tuples = new LinkedList<KeyValuePair<K, V>>();
		shuffler = s;
		phase = p;
	}

	public void collect(K key, V value) {
		KeyValuePair<K, V> kvp = new KeyValuePair<K, V>(key, value);
		tuples.add(kvp);
		if (phase.equals("map")) { collectAndShuffle(kvp); }
		else if (phase.equals("reduce")) { collectAndSend(kvp); }
		else { System.out.println("Error, unknown phase"); }
	}
	
	public synchronized void collect(KeyValuePair<K, V> kvp){
	    tuples.add(kvp);
	}
	
	public List<KeyValuePair<K, V>> getTuples() {
	    return tuples;
	}
	
	public void setShuffler(Shuffler s) {
	    shuffler = s;
	}
	
	/**
	 * Called after map, shuffles k,v pairs to new nodes
	 * @param kvp
	 */
	public void collectAndShuffle(KeyValuePair<K, V> kvp) {
	    shuffler.assignToMachine(kvp);
	}
	
	/**
	 * Called after reduce, sends all k,v pairs to host
	 * @param kvp
	 */
	public void collectAndSend(KeyValuePair<K, V> kvp) {
	    shuffler.assignToHost(kvp);
	}

}