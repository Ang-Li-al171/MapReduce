package map;

import output.OutputCollector;

public interface Mapper {
    public void map(String file, OutputCollector o);
    public void receiveEOF(int count);
    public void jobDoneCount();
    public void incrementSentCounts(int n);
}