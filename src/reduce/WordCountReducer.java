package reduce;

import network.NetworkMaster;
import output.OutputCollector;

public class WordCountReducer<K, V> implements Reducer<K, V> {

	private NetworkMaster reduceNetwork;
	
	public WordCountReducer(NetworkMaster n){
		reduceNetwork = n;
	}
	
	@Override
	public void reduceCurrent(OutputCollector<K, V> output){
		//Shuffle

		//Sort

		//Reduce
		
	}
}