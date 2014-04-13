package map;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;
import output.Distributor;
import keyvaluepair.KeyValuePair;
import network.NetworkMaster;


public class WordCountMapper implements Mapper<String, Integer> {

    private NetworkMaster myNetwork;
    private int jobDone;
    private int[] sentCounts;

    public WordCountMapper (NetworkMaster network) {
        myNetwork = network;
        jobDone = 0;
        sentCounts = new int[network.getNodeListSize()];
    }

    @Override
    public void map (String s, Distributor<String, Integer> o) {
    	String[] wordList = s.split(" ");
    	for (String rawWord: wordList) {
    	    String word = processWord(rawWord);
    	    o.collectAndShuffle(word, 1);       
    	}
    	System.out.println("Finished a map");
    }

    private String processWord (String rawWord) {
        String s = rawWord.replaceAll("[()?:\"!.,;]+", "");
        return s.toLowerCase();
    }

    @Override
    public synchronized void jobDoneCount () {
        jobDone++;
        System.out.println("Increment: JOBDONE COUNTER: " + jobDone);
        notifyAll();
    }

    @Override
    public synchronized void receiveEOF (int count) {
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
    
    public void incrementSentCounts(int n) {
        synchronized(this){
            sentCounts[n]++;
        }
    }
}
