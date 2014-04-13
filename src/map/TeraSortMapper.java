package map;

import network.NetworkMaster;
import output.Distributor;

public class TeraSortMapper implements Mapper<String, Integer>{
	private NetworkMaster myNetwork;
	private int jobDone;
    private int[] sentCounts;
	
    public TeraSortMapper (NetworkMaster network) {
        myNetwork = network;
        jobDone = 0;
        sentCounts = new int[network.getNodeListSize()];
    }
    
	@Override
	public void map(String file, Distributor<String, Integer> o) {
		//Create trie tree
		//Split words and run through trie tree
		//Pass words to corresponding reducers.
		
	}

	@Override
	public void receiveEOF(int count) {
		System.out.println("MAP EOF received!");
        while (jobDone < count) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
        System.out.println("START sending REDUCEEOF");
        myNetwork.sendReduceEOFToAll(sentCounts);
	}

	@Override
	public void jobDoneCount() {
		jobDone++;
        System.out.println("Increment: JOBDONE COUNTER: " + jobDone);
        notifyAll();
	}

	@Override
	public void incrementSentCounts(int n) {
		synchronized(this){
            sentCounts[n]++;
        }
	}

}
