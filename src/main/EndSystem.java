package main;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;
import network.NetworkCodes;
import network.NetworkMaster;
import output.*;
import preprocess.PreProcessor;
import preprocess.WordCountPreProcessor;

/**
The main user program, and the only file the programmer sees.
This is where the programmer creates the specifics for a job and runs it.
 */

public class EndSystem {

    private Distributor<String, Integer> myOutput;
    private PreProcessor myPreProcessor;
    private NetworkMaster myNetwork;

    public EndSystem(){       
        myNetwork = new NetworkMaster();
    }
    
    //TODO: more refactoring can go here
    public void runTask(String type, String file) {
    	long startTime = System.currentTimeMillis();
    	if (type.equals("wordcount")) {
    		//notify job choice to peers
            myNetwork.sendMsgToAll(Integer.toString(NetworkCodes.WORDCOUNT));
            System.out.println("Initiating MR job, timer starts...");
            
            myPreProcessor = new WordCountPreProcessor(myNetwork);
            myPreProcessor.preProcess(file);
            
            myNetwork.registerTimer(startTime);
        }
    }
    
    public void runTeraSort(String file) {
    	//Pick Samples
   		//Sort Samples
    	//Find splits
  		//Pass <line, splits> to Mappers
    }

    public void joinHost(String ownIP, String ownPort, String ip, String port){
        myNetwork.requestJoin(ownIP, ownPort, ip, port);
    }

    public void hostNetwork(String port){
        myNetwork.startListening(Integer.parseInt(port));
    }
}