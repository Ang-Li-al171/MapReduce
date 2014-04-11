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

	public OutputCollector() {
		tuples = new LinkedList<KeyValuePair<K, V>>();
	}

	public void collect(K key, V value) {
		KeyValuePair<K, V> t = new KeyValuePair<K, V>(key, value);
		tuples.add(t);
	}
	
	public synchronized void collect(KeyValuePair<K, V> kvp){
	    tuples.add(kvp);
	}

}