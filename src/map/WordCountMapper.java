package map;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;
import output.OutputCollector;

import keyvaluepair.KeyValuePair;

import network.NetworkMaster;

public class WordCountMapper implements Mapper {

    private NetworkMaster myNetwork;
    private boolean mapEOF;
    private int jobCounter;

    public WordCountMapper (NetworkMaster network) {
        myNetwork = network;
        mapEOF = false;
        jobCounter = 0;
    }

    @Override
    public void map (String s, OutputCollector o) {
    	String[] wordList = s.split(" ");
    	for (String rawWord: wordList) {
    	    String word = processWord(rawWord);
    	    o.collectAndSend(new KeyValuePair<String, Integer>(word, 1));       
    	}
    	System.out.println("Finished a map");
    }
    
    private String processWord(String rawWord) {
    	String s = rawWord.replaceAll("[()?:\"!.,;]+", "");

    	return s.toLowerCase();
    }
    
    @Override
    public synchronized void incrementCounter() {
    	jobCounter++;
    	System.out.println("INCRE: CURRENT JOB COUNTER: " + jobCounter);
    }
    
    @Override
    public synchronized void decrementCounter() {
    	jobCounter--;
    	System.out.println("DECRE: CURRENT JOB COUNTER: " + jobCounter);
    	notifyAll();
    }

    @Override
    public synchronized void receiveEOF() {
    	mapEOF = true;
    	System.out.println("MAPEOF received!");
    	while (jobCounter != 0) {
    		try {
				wait();
			} catch (InterruptedException e) {
				//e.printStackTrace();
			}
    	}
    	System.out.println("START sending REDUCEEOF");
    	StringBuilder sb = new StringBuilder();
    	sb.append("4000 ");
    	sb.append(myNetwork.getPort());
    	myNetwork.sendMsgToAll(sb.toString());

    }
}
