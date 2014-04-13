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
	private int counter;
    private int[] jobCounts;
    
    public TeraSortPreProcessor(NetworkMaster network) {
		myNetwork = network;
		counter = 0;
		jobCounts = new int[network.getNodeListSize()];
	}
	
	@Override
	public void preProcess(String file) {
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
        System.out.println("Sorted Samples");
        for (int i = 0; i<samples.length; i++) System.out.println(samples[i]);
    	//Find splits
        String[] splits = findSplits(samples);
        System.out.println("Splits");
        for (int i = 0; i<splits.length; i++) System.out.println(splits[i]);
  		//Pass <line, splits> to Mappers

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

}
