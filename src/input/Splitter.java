package input;

import java.util.*;

/**
 * Divides the input among the nodes in the cluster
 * @author weideng
 *
 */
public class Splitter {
    private static List<String> nodes;
    private static int numNodes;
    private static int counter;
    
    public Splitter(List<String> n) {
        nodes = n;
        numNodes = n.size();
        counter = 0;
    }
    
    protected void assignToNode(String word) {
        String currentIp = nodes.get(counter);
        System.out.println("Sending word: '" + word + "' to machine: " + currentIp);
        //TODO: Networking call to send to the ip
        incrementCounter();     
    }
    
    private void incrementCounter() {
        counter++;
        if (counter == numNodes) { counter = 0; }
    }

}
