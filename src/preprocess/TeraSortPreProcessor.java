package preprocess;

import input.FileReader;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import network.NetworkCodes;
import network.NetworkMaster;

public class TeraSortPreProcessor implements PreProcessor{

	private NetworkMaster myNetwork;
	private List<String> words;
    private int[] jobCounts;
    private int counter = 0;
    
    public TeraSortPreProcessor(NetworkMaster network) {
		myNetwork = network;
	}
	
	@Override
	public void preProcess(String file) {
		jobCounts = new int[myNetwork.getNodeListSize()];
		
		FileReader fr = new FileReader();
        try {
            words = fr.readAndSplitByWord(file);	//Probably read by words
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        //Pick Samples
        String[] samples = pickSamples();
        //Sort Samples
        Arrays.sort(samples);
        for (int i = 0; i<samples.length; i++) System.out.println(samples[i]);
    	
        //Find splits
        String[] splits = findSplits(samples);
        for (int i = 0; i<splits.length; i++) System.out.println("splits: " + splits[i]);
        
        StringBuilder sb = new StringBuilder();
        sb.append(NetworkCodes.TERASORTSPLITER);
        sb.append(" ");
        for (int i = 0; i<splits.length; i++) {
        	sb.append(splits[i]);
        	if (i != splits.length-1) sb.append(" ");
        }
        myNetwork.sendMsgToAll(sb.toString());
  		
        //Pass <line, splits> to Mappers
        for (String word: words) {
        	//System.out.printf("Sending word: %s to machine: %d in the node list\n", word, counter);
            String modified = word.replaceAll("[^a-zA-Z ]", "").toLowerCase();
            if (modified.length() < 1) continue;
            myNetwork.sendMsgToNode(counter, modified);
            jobCounts[counter]++;
            counter++;
            if (counter == myNetwork.getNodeListSize()) counter = 0;
        	
//        	int index = findPosition(word, splits);
//        	System.out.printf("Sending word: %s to machine: %d in the node list\n", word, index);
//        	jobCounts[index]++;
//        	myNetwork.sendMsgToNode(index, word);
        }
        
        myNetwork.sendMapEOFToAll(jobCounts);
	}
	
	public String[] pickSamples() {
		int size = myNetwork.getNodeListSize()*5;
		String[] results = new String[size];
		
		//TODO: This is a really bad way of sampling :/!
		//TODO: deal with index out of bound with arbitrary size of samples
		for (int i = 0; i<size; i++) results[i] = words.get(i);
		return results;
	}
	
	public String[] findSplits(String[] samples) {
		int size = myNetwork.getNodeListSize();
		int interval = samples.length/size;
		String[] results = new String[size-1];
		int currentIndex = interval;
		for (int i = 0; i<size-1; i++) {
			results[i] = samples[currentIndex];
			currentIndex = currentIndex + interval;
		}
		
		return results;
	}
	
	public int findPosition(String s, String[] splits) {
		int index = 0;
		while (s.compareTo(splits[index]) > 0) {
			if (index == splits.length-1) break;
			else index++;
		}
		return index;
	}

}
