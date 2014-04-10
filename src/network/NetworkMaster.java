package network;

import java.util.List;
import keyvaluepair.KeyValuePair;

public interface NetworkMaster<K, V> {
	
	public List<Node> getNodes();
	public int getNodeListSize();
	public void register(String ip, String port);
	public boolean blockUntilNextAnswer();
	public void startListening(int port);
	public void requestJoin(String ownIP, String ownPort, String ip, String port);
	public void sendWordToNode(int index, K word);
	public void sendKVPToNode(int index, KeyValuePair<K, V> kvp);
	public void collectKVP(KeyValuePair<K, V> kvp);
	
}
