package reduce;

import java.util.Iterator;

import network.NetworkMaster;

import keyvaluepair.KeyValuePair;
import output.Distributor;

public class TeraSortReducer implements Reducer{
	private NetworkMaster myNetwork;
	
	 public TeraSortReducer (NetworkMaster network) {
	        myNetwork = network;
	 }
	
	@Override
	public void reduce(Object key, Iterator values, Distributor o) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addKVP(KeyValuePair kvp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveEOF(int port, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void jobDoneCount() {
		// TODO Auto-generated method stub
		
	}

}
