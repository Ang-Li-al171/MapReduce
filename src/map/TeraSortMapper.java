package map;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import network.NetworkMaster;
import output.Distributor;

public class TeraSortMapper implements Mapper<String, Integer>{
//    private class Node{
//    	char myKey;
//    	List<Node> myChildren;
//        Node(char s) {
//        	myKey = s;
//        	myChildren = new ArrayList<Node>();
//        }
//        Node() {
//        	myChildren = new ArrayList<Node>();
//        }
//    }
    
	private NetworkMaster myNetwork;
	private int jobDone;
    private int[] sentCounts;
    private String[] mySpliters;
    //private Node myHead = new Node();
    
    public TeraSortMapper (NetworkMaster network) {
        myNetwork = network;
        jobDone = 0;
        sentCounts = new int[network.getNodeListSize()];
    }
    
    public void receiveSpliter(String s) {
    	mySpliters = s.split(" ");	//NOTE: start from index 1;
    }
    
//    private void addToTrie(String s, int index) {
//    	Node current = myHead;
//    	for (int i = 0; i<s.length(); i++) {
//    		char c = s.charAt(i);
//    		boolean found = false;
//    		
//    		List<Node> children = current.myChildren;
//    		for (Node n: children) {
//    			if (n.myKey == c) {
//    				current = n;
//    				found = true;
//    				break;
//    			}
//    		}
//    		if (!found) {
//    			Node temp = new Node(c);
//    			children.add(temp);
//    			current.myChildren = children;
//    			current = temp;
//    		}
//    	}
//    	current.myChildren.add(new Node((char)index));	//if smaller than splitter, goes to index
//    }
    
	@Override
	public void map(String word, Distributor<String, Integer> o) {
		int index = 0;
		while (word.compareTo(mySpliters[index+1]) > 0) {
			index++;
			if (index == mySpliters.length-1) break;
		}
		o.collectAndSendToIndex(index, word, index);
	}

	@Override
	public synchronized void receiveEOF(int count) {
		System.out.println("MAP EOF received!");
        while (jobDone < count) {
            try {
                wait();
            }
            catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
        System.out.println("START sending REDUCEEOF");
        myNetwork.sendReduceEOFToAll(sentCounts);
	}

	@Override
	public synchronized void jobDoneCount() {
		jobDone++;
        System.out.println("Increment: JOBDONE COUNTER: " + jobDone);
        notifyAll();
	}

	@Override
	public void incrementSentCounts(int n) {
		synchronized(this){
            sentCounts[n]++;
        }
	}

}
