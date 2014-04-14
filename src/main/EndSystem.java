package main;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.NetworkCodes;
import network.NetworkMaster;
import output.*;
import postprocess.PostProcess;
import postprocess.TeraSortPostProcess;
import postprocess.WordCountPostProcess;
import preprocess.PreProcessor;
import preprocess.TeraSortPreProcessor;
import preprocess.WordCountPreProcessor;

/**
The main user program, and the only file the programmer sees.
This is where the programmer creates the specifics for a job and runs it.
 */

public class EndSystem {

    private Distributor<String, Integer> myOutput;
    private PreProcessor myPreProcessor;
    private NetworkMaster myNetwork = new NetworkMaster();
    private String[] myTasks = {"mrwordcount", "mrterasort"};
    private PreProcessor[] myTaskProcessors = {new WordCountPreProcessor(myNetwork),
    											new TeraSortPreProcessor(myNetwork)};
    private PostProcess[] myPostProcessors = {new WordCountPostProcess(myNetwork),
			new TeraSortPostProcess(myNetwork)};
    private Integer[] myTaskCodes = {NetworkCodes.WORDCOUNT, NetworkCodes.TERASORT};
    
    public void runTask(String type, String file) {
    	long startTime = System.currentTimeMillis();
    	
    	List<String> myTasksList = Arrays.asList(myTasks);
    	myNetwork.sendMsgToAll(Integer.toString(myTaskCodes[myTasksList.indexOf(type)]));
        System.out.println("Initiating MR job, timer starts...");
        
        myPreProcessor = myTaskProcessors[myTasksList.indexOf(type)];
        myPreProcessor.preProcess(file);
        
        myNetwork.setPostProcessor(myPostProcessors[myTasksList.indexOf(type)]);
        
        myNetwork.registerTimer(startTime);

    }

    public void joinHost(String ownIP, String ownPort, String ip, String port){
        myNetwork.requestJoin(ownIP, ownPort, ip, port);
    }

    public void hostNetwork(String port){
        myNetwork.startListening(Integer.parseInt(port));
    }
}