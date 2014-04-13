package map;

import output.Distributor;

public interface Mapper<K,V> {
    public void map(String file, Distributor<K,V> o);
    public void receiveEOF(int count);
    public void jobDoneCount();
    public void incrementSentCounts(int n);
}