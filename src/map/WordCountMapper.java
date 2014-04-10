package map;

import input.FileReader;
import input.Splitter;
import java.io.FileNotFoundException;
import network.NetworkMaster;

public class WordCountMapper implements Mapper {

    private NetworkMaster mapNetwork;

    public WordCountMapper (NetworkMaster n) {
        mapNetwork = n;
    }

    @Override
    public void map (String file) {

        Splitter s = new Splitter(mapNetwork.getNodeListSize(), mapNetwork); // Programmer can customize the splitter they use
        FileReader fr = new FileReader(s); // Programmer can also customize the reader they use
        try {
            fr.read(file);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
