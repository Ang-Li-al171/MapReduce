package main;

import input.FileReader;
import input.Splitter;

import java.io.FileNotFoundException;

import network.NetworkMaster;
import map.*;
import reduce.*;
import output.*;

/**
The main user program, and the only file the programmer sees.
This is where the programmer creates the specifics for a job and runs it.
 */

public class EndSystem {

    private Mapper myMapper;
    private Reducer<String, Integer> myReducer;
    private OutputCollector<String, Integer> myOutput;
    private NetworkMaster myNetwork;

    public EndSystem(){
        myOutput = new OutputCollector<String, Integer>();
        myNetwork = new NetworkMaster(myOutput);
    }
    
    public void runWordCount(String file) { //Specify the job configurations, mapper, reducer, etc

        long startTime = System.currentTimeMillis();
        
        notifyJobToNetwork("3000");

        System.out.println("Initiating MR job");
        myMapper = new WordCountMapper(myNetwork);
        myReducer = new WordCountReducer<String, Integer>(myNetwork);
        
        
        Splitter s = new Splitter(myNetwork.getNodeListSize(), myNetwork); // Programmer can customize the splitter they use
        FileReader fr = new FileReader(s); // Programmer can also customize the reader they use
        try {
            fr.read(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        

        System.out.println("Running reducing job");
        while(myNetwork.blockUntilNextAnswer()){
            myReducer.reduceCurrent(myOutput);
        }

        long endTime = System.currentTimeMillis();

        System.out.printf("Job done! Take taken: %d milliseconds", endTime-startTime);
    }

    private void notifyJobToNetwork(String msg) {
		myNetwork.sendMsgToAll(msg);
		
	}

	public void joinHost(String ownIP, String ownPort, String ip, String port){
        myNetwork.requestJoin(ownIP, ownPort, ip, port);
    }

    public void hostNetwork(String port){
        myNetwork.startListening(Integer.parseInt(port));
    }
}