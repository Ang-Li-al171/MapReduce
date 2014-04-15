package postprocess;

import java.io.PrintWriter;
import network.NetworkMaster;
import keyvaluepair.KeyValuePair;


public class WordCountPostProcess implements PostProcess {

    NetworkMaster myNetwork;
    PrintWriter output;

    public WordCountPostProcess (NetworkMaster network) {
        myNetwork = network;
        try {
            output = new PrintWriter("src/files/testOutput.txt", "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveKVP (KeyValuePair<String, Integer> kvp) {
        output.println("RESULT: " + kvp.getKey() + " "
                           + kvp.getValue() + " Time spent:" + myNetwork.timeSpent(System.currentTimeMillis()));
        output.flush();
    }

}
