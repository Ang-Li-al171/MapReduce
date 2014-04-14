package reduce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import network.NetworkMaster;

import keyvaluepair.KeyValuePair;
import output.Distributor;

public class TeraSortReducer implements Reducer<String, Integer>{
	private NetworkMaster myNetwork;
	private HashSet<Integer> reduceEOF;
	private List<String> words;
    private int jobDone;
    private int totalRevCount;
	
	 public TeraSortReducer (NetworkMaster network) {
	        myNetwork = network;
	        reduceEOF = new HashSet<Integer>();
	        jobDone = 0;
	        totalRevCount = 0;
	        words = new ArrayList<String>();
	 }
	
	@Override
	public synchronized void addKVP(KeyValuePair<String, Integer> kvp) {
		words.add(kvp.getKey());
	}

	@Override
	public synchronized void receiveEOF(int port, int count) {
		reduceEOF.add(port);
        totalRevCount +=count;
        
        if (reduceEOF.size() == myNetwork.getNodeListSize()) {
            while (jobDone < totalRevCount) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                     e.printStackTrace();
                }
            }
            reduce();
            System.out.println("REDUCE DONE");
        }
	}

	@Override
	public synchronized void jobDoneCount() {
		jobDone++;
        System.out.println("Increment: JOBDONE COUNT: " + jobDone);
        notifyAll();
		
	}

	public void reduce() {
		Distributor output = new Distributor(myNetwork);
		String[] wordsArray = new String[words.size()];
		for (int i = 0; i<words.size(); i++) wordsArray[i] = words.get(i);
		Arrays.sort(wordsArray);
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i< wordsArray.length; i++) {
			sb.append(wordsArray[i]);
			sb.append(" ");
		}
		output.collectAndSendToHost(sb.toString(), myNetwork.getPort());
	}

}
