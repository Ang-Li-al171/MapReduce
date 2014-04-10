package network;

import java.util.List;

public interface NetworkMaster {
	
	public List<Node> getNodes();
	public void register(String ip, String port);
	public boolean blockUntilNextAnswer();
	public void startListening(int port);
	public void requestJoin(String ownIP, String ownPort, String ip, String port);
}
