package main;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;
import network.NetworkCodes;
import network.NetworkMaster;
import output.*;

/**
The main user program, and the only file the programmer sees.
This is where the programmer creates the specifics for a job and runs it.
 */

public class EndSystem {

    private OutputCollector<String, Integer> myOutput;
    private NetworkMaster myNetwork;

    public EndSystem(){
        myOutput = new OutputCollector<String, Integer>();
        myNetwork = new NetworkMaster(myOutput);
    }
    
    public void runWordCount(String file) { //Specify the job configurations, mapper, reducer, etc
        
        //notify job choice to peers
        myNetwork.sendMsgToAll(Integer.toString(NetworkCodes.WORDCOUNT));
        System.out.println("Initiating MR job, timer starts...");
        Splitter s = new Splitter(myNetwork.getNodeListSize(), myNetwork); // Programmer can customize the splitter they use
        FileReader fr = new FileReader(s); // Programmer can also customize the reader they use
        
        long startTime = System.currentTimeMillis(); // do we start the timer here?
        
        try {
            fr.read(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        myNetwork.sendMapEOFToAll(fr.getCounts());     
        myNetwork.registerTimer(startTime);
    }

    public void joinHost(String ownIP, String ownPort, String ip, String port){
        myNetwork.requestJoin(ownIP, ownPort, ip, port);
    }

    public void hostNetwork(String port){
        myNetwork.startListening(Integer.parseInt(port));
    }
}