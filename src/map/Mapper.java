package map;

public interface Mapper {
	
    public void map(String file);
    public void receiveEOF();
    public void incrementCounter();
    public void decrementCounter();
}