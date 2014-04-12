package map;

import keyvaluepair.KeyValuePair;
import network.NetworkMaster;


public class WordCountMapper implements Mapper {

    private NetworkMaster myNetwork;
    private int jobDone;
    private int[] sentCounts;

    public WordCountMapper (NetworkMaster network) {
        myNetwork = network;
        jobDone = 0;
        sentCounts = new int[network.getNodeListSize()];
    }

    @Override
    public void map (String s) {
        String[] wordList = s.split(" ");
        for (String rawWord : wordList) {
            String word = processWord(rawWord);
            int n = Math.abs(word.trim().hashCode() % myNetwork.getNodeListSize());
            synchronized(this){
                sentCounts[n]++;
            }
            myNetwork.sendKVPToNode(n, new KeyValuePair<String, Integer>(word, 1));
        }
        System.out.println("Finished mapping one line");
    }

    private String processWord (String rawWord) {
        String s = rawWord.replaceAll("[()?:\"!.,;]+", "");

        return s.toLowerCase();
    }

    @Override
    public synchronized void jobDoneCount () {
        jobDone++;
        System.out.println("Increment: JOBDONE COUNTER: " + jobDone);
        notifyAll();
    }

    @Override
    public synchronized void receiveEOF (int count) {
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
}
