package map;

import output.OutputCollector;

public interface Mapper<K,V> {
    public void map(String file, OutputCollector<K,V> o);
    public void receiveEOF(int count);
    public void jobDoneCount();
    public void incrementSentCounts(int n);
}