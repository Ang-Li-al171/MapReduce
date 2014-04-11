package reduce;

import network.NetworkMaster;
import output.OutputCollector;

public class WordCountReducer<K, V> implements Reducer<K, V> {

	private NetworkMaster reduceNetwork;
	
	public WordCountReducer(NetworkMaster myNetwork){
		reduceNetwork = myNetwork;
	}
	
	@Override
	public void reduceCurrent(OutputCollector<K, V> output){
		//Shuffle

		//Sort

		//Reduce
		
	}
}