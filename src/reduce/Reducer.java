package reduce;

import output.*;
import java.io.IOException;

public interface Reducer<K, V> {
	
	public void reduceCurrent(OutputCollector<K, V> output) throws IOException;
	
}