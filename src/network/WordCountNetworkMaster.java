package network;

import java.util.LinkedList;
import java.util.List;

import output.OutputCollector;

public class WordCountNetworkMaster implements NetworkMaster{

	List<String> myIPs = new LinkedList<String>();
	List<Integer> myPorts = new LinkedList<Integer>();
	OutputCollector<String, Integer> myOutput;
	
	public WordCountNetworkMaster(OutputCollector<String, Integer> o){
		myOutput = o;
	}
	
	@Override
	public List<String> getIPs() {
		return myIPs;
	}

	@Override
	public List<Integer> getPorts() {
		return myPorts;
	}

	public void register(String iP, int port){
		myIPs.add(iP);
		myPorts.add(port);
	}

	@Override
	public boolean blockUntilNextAnswer() {
		// TODO Auto-generated method stub
		return false;
	}
}
