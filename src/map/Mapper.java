package map;
import output.*;
import java.io.IOException;

public interface Mapper<K1,V1,K2,V2> {
	public void map(int key, String value, OutputCollector output) throws IOException;
}