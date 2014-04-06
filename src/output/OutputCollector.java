package output;
import java.util.*;
import java.io.IOException;
/**
Collects the <key,value> tuples created by map.
For multi-node jobs, networking calls will be made from
here to send tuples to other nodes.
*/
public class OutputCollector {

	public class Tuple<K, V> { 
	  public final K x; 
	  public final V y; 
	  public Tuple(K x, V y) { 
	    this.x = x; 
	    this.y = y; 
	  } 
	} 

	private ArrayList<Tuple> tuples; //Programmer should not be able to access this

	public OutputCollector() {
		tuples = new ArrayList<Tuple>();
	}

	public void collect(int key, String value) {
		Tuple<Integer, String> t = new Tuple<Integer, String>(key, value);
		tuples.add(t);
	}


}