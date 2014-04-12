package map;

import output.OutputCollector;

public interface Mapper {
	
    public void map(String file, OutputCollector o);
    public void receiveEOF();
    public void incrementCounter();
    public void decrementCounter();
}