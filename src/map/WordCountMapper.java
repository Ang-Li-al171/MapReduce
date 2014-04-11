package map;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;

import keyvaluepair.KeyValuePair;

import network.NetworkMaster;

public class WordCountMapper implements Mapper {

    private NetworkMaster myNetwork;

    public WordCountMapper (NetworkMaster network) {
        myNetwork = network;
    }

    @Override
    public void map (String s) {
    	String[] wordList = s.split(" ");
    	for (String word: wordList) {
    		int n = Math.abs(word.trim().hashCode() % myNetwork.getNodeListSize());
            myNetwork.sendKVPToNode(n, new KeyValuePair<String, Integer>(word, 1));
    	}
        
    }

}
