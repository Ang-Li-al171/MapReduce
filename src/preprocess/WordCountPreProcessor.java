package preprocess;

import java.io.FileNotFoundException;
import java.util.List;

import network.NetworkMaster;

import input.FileReader;

public class WordCountPreProcessor implements PreProcessor{
	
	private NetworkMaster myNetwork;
	private List<String> lines;
	private int counter;
    private int[] jobCounts;
	
	public WordCountPreProcessor(NetworkMaster network) {
		myNetwork = network;
		counter = 0;
	}

	@Override
	public void preProcess(String file) {
		jobCounts = new int[myNetwork.getNodeListSize()];
		
		FileReader fr = new FileReader();
        try {
            lines = fr.readAndSplitByLine(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        //TODO: put it here now for simplicity. Can later put back to Splitter (need to be more flexible).
        for (String line: lines) {
        	//System.out.printf("Sending line: %s to machine: %d in the node list\n", line, counter);
            jobCounts[counter]++;
            myNetwork.sendMsgToNode(counter, line);
            counter++;
            if (counter == myNetwork.getNodeListSize()) counter = 0;
        }
        
        myNetwork.sendMapEOFToAll(jobCounts);
	}
}
