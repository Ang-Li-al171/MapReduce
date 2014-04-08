package network;

import java.util.List;

public interface NetworkMaster {
	
	public List<String> getIPs();
	public List<Integer> getPorts();
	public boolean blockUntilNextAnswer();
}
