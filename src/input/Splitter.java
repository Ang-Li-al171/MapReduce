package input;

import network.NetworkMaster;


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
    private int[] counts;
    
    public Splitter(int nodeCount, NetworkMaster network) {
        myNetwork = network;
        numNodes = nodeCount;
        counter = 0;
        counts = new int[nodeCount];
    }
    
    public int[] getMapCounts(){
        return counts;
    }
    
    protected void assignToNode(String line) {
        System.out.printf("Sending line: %s to machine: %d in the node list\n", line, counter);
        counts[counter]++;
        myNetwork.sendMsgToNode(counter, line);
        incrementCounter();     
    }
    
    private void incrementCounter() {
        counter++;
        if (counter == numNodes) { counter = 0; }
    }

}
