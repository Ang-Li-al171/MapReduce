package reduce;
import output.*;
import java.util.*;
import java.io.IOException;

public interface Reducer<K2,V2,K3,V3> {
	public void reduce(int key, Iterator<String> values, OutputCollector output) throws IOException;
}