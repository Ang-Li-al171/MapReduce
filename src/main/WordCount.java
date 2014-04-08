package main;

import network.NetworkMaster;
import network.WordCountNetworkMaster;
import map.*;
import reduce.*;
import output.*;

/**
The main user program, and the only file the programmer sees.
This is where the programmer creates the specifics for a job and runs it.
*/

public class WordCount {

	private WordCountMapper myMapper;
	private WordCountReducer<String, Integer> myReducer;
	private OutputCollector<String, Integer> myOutput;
	private NetworkMaster myNetwork;

	public void run() { //Specify the job configurations, mapper, reducer, etc
		
		long startTime = System.currentTimeMillis();
		
		System.out.println("Initiating MR job");
		myOutput = new OutputCollector<String, Integer>();
		myNetwork = new WordCountNetworkMaster(myOutput);
		myMapper = new WordCountMapper(myNetwork);
		myReducer = new WordCountReducer<String, Integer>(myNetwork);
		
		System.out.println("Running mapping job");
		myMapper.map(myNetwork.getIPs(), myNetwork.getPorts());
		
		System.out.println("Running reducing job");
		while(myNetwork.blockUntilNextAnswer()){
			myReducer.reduceCurrent(myOutput);
		}
		
		long endTime = System.currentTimeMillis();
		
		System.out.printf("Job done! Take taken: %d milliseconds", endTime-startTime);
	}

}