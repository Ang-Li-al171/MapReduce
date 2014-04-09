package network;

/**
 * Object representation of a node/machine in the MR cluster
 * @author weideng
 *
 */
public class Node {
    private String ip;
    private int port;
    
    public Node(String _ip, String _port) {
        ip = _ip;
        port = Integer.parseInt(_port);
    }
    
    public String getIp() { return ip; }
    
    public int getPort() { return port; }

}
