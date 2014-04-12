package reduce;

import output.*;
import java.io.IOException;
import java.util.Iterator;

import keyvaluepair.KeyValuePair;

public interface Reducer<K, V> {
	
	public void reduce(String key, Iterator<Integer> values);
	public void addKVP(KeyValuePair kvp);
	public void receiveEOF(int port);
    public void incrementCounter();
    public void decrementCounter();
}