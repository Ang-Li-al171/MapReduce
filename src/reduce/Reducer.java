package reduce;

import java.util.Iterator;
import keyvaluepair.KeyValuePair;


public interface Reducer<K, V> {

    public void reduce (K key, Iterator<V> values);

    public void addKVP (KeyValuePair<K, V> kvp);

    public void receiveEOF (int port);

    public void incrementCounter ();

    public void decrementCounter ();
}
