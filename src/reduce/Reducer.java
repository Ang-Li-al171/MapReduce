package reduce;

import java.util.Iterator;
import output.Distributor;
import keyvaluepair.KeyValuePair;


public interface Reducer<K, V> {

    public void addKVP (KeyValuePair<K, V> kvp);

    public void receiveEOF (int port, int count);

    public void jobDoneCount ();

}
