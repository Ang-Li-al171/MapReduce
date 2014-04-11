package map;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;

import network.NetworkMaster;

public class WordCountMapper implements Mapper {

    private NetworkMaster mapNetwork;

    public WordCountMapper (NetworkMaster myNetwork) {
        mapNetwork = myNetwork;
    }

    @Override
    public void map (String file) {

        
    }

}
