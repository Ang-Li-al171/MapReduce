package input;

import network.*;

/**
 * Divides the input among the nodes in the cluster. There are many types of splitter algorithms
 * such as slit by file, split by line, split by word, etc. This one is split by word.
 * @author weideng
 *
 */
public class Splitter {
    private static int numNodes;
    private static int counter;
    private NetworkMaster myNetwork;
    
    public Splitter(int nodeCount, NetworkMaster mapNetwork) {
        myNetwork = mapNetwork;
        numNodes = nodeCount;
        counter = 0;
    }
    
    protected void assignToNode(String word) {
        myNetwork.sendWordToNode(counter, word);
        System.out.printf("Sending word: %s to machine: %d in the node list\n", word, counter);
        incrementCounter();     
    }
    
    private void incrementCounter() {
        counter++;
        if (counter == numNodes) { counter = 0; }
    }

}
