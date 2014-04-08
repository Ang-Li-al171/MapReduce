package map;

import java.util.List;
import network.NetworkMaster;


public class WordCountMapper implements Mapper {
	
	private NetworkMaster mapNetwork;
	
	public WordCountMapper(NetworkMaster n){
		mapNetwork = n;
	}
	
	@Override
	public void map(List<String> ips, List<Integer> ports) {
		// split the task and send out to peers
	}
}
