package map;

import keyvaluepair.KeyValuePair;
import network.NetworkCodes;
import network.NetworkMaster;

public class WordCountMapper implements Mapper {

    private NetworkMaster myNetwork;
    private int jobCounter;

    public WordCountMapper (NetworkMaster network) {
        myNetwork = network;
        jobCounter = 0;
    }

    @Override
    public void map (String s) {
    	String[] wordList = s.split(" ");
    	for (String rawWord: wordList) {
    		String word = processWord(rawWord);
    		int n = Math.abs(word.trim().hashCode() % myNetwork.getNodeListSize());
            myNetwork.sendKVPToNode(n, new KeyValuePair<String, Integer>(word, 1));
    	}
    	System.out.println("Finished mapping one line");
    }
    
    private String processWord(String rawWord) {
    	String s = rawWord.replaceAll("[()?:\"!.,;]+", "");

    	return s.toLowerCase();
    }
    
    @Override
    public synchronized void incrementCounter() {
    	jobCounter++;
    	System.out.println("Increment: JOB COUNTER: " + jobCounter);
    }
    
    @Override
    public synchronized void decrementCounter() {
    	jobCounter--;
    	System.out.println("Decrement: JOB COUNTER: " + jobCounter);
    	notifyAll();
    }

    @Override
    public synchronized void receiveEOF() {
    	System.out.println("MAP EOF received!");
    	while (jobCounter != 0) {
    		try {
				wait();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
    	}
    	System.out.println("START sending REDUCEEOF");
    	StringBuilder sb = new StringBuilder();
    	sb.append(Integer.toString(NetworkCodes.REDUCEEOF));
    	sb.append(" ");
    	sb.append(myNetwork.getPort());
    	myNetwork.sendMsgToAll(sb.toString());

    }
}
