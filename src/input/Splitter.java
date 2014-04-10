package input;

import java.util.*;
import network.*;
import network.client.*;

/**
 * Divides the input among the nodes in the cluster. There are many types of splitter algorithms
 * such as slit by file, split by line, split by word, etc. This one is split by word.
 * @author weideng
 *
 */
public class Splitter {
    private static List<Node> nodes;
    private static int numNodes;
    private static int counter;
    private static int STANDARD_TIMEOUT = 1000;
    
    public Splitter(List<Node> n ) {
        nodes = n;
        numNodes = n.size();
        counter = 0;
    }
    
    protected void assignToNode(String word) {
        Node current = nodes.get(counter);
        String ip = current.getIp();
        String port = Integer.toString(current.getPort());
        System.out.println("Sending word: '" + word + "' to machine: " + ip + " at port: " + port);
        //Networking call to send to the ip
        TCPClient network = new TCPClient(current,STANDARD_TIMEOUT);
        network.sendObjectToServer("String", word);
        incrementCounter();     
    }
    
    private void incrementCounter() {
        counter++;
        if (counter == numNodes) { counter = 0; }
    }

}
