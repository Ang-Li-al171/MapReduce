package reduce;

import java.util.Iterator;
import keyvaluepair.KeyValuePair;


public interface Reducer<K, V> {

    public void reduce (K key, Iterator<V> values);

    public void addKVP (KeyValuePair<K, V> kvp);

    public void receiveEOF (int port, int count);

    public void jobDoneCount ();
}
