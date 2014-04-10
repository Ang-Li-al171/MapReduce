package map;

import java.util.List;
import network.NetworkMaster;
import network.Node;


public class WordCountMapper implements Mapper {
	
	private NetworkMaster mapNetwork;
	
	public WordCountMapper(NetworkMaster n){
		mapNetwork = n;
	}
	
	@Override
	public void map(List<Node> nodes) {
		// split the task and send out to peers
	}

}
