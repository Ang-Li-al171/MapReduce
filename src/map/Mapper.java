package map;

public interface Mapper {
	
    public void map(String file);
    public void receiveEOF(int count);
    public void jobDoneCount();
}