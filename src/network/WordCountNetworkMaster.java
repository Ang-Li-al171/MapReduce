package network;

import java.util.LinkedList;
import java.util.List;
import network.client.TCPClient;
import output.OutputCollector;


public class WordCountNetworkMaster implements NetworkMaster {

    private List<Node> myNodes;
    private OutputCollector<String, Integer> myOutput;
    private AcceptConnection myServer;
    private List<TCPClient> myClients;
    private boolean unStarted;

    public WordCountNetworkMaster (OutputCollector<String, Integer> o) {
        myOutput = o;
        myNodes = new LinkedList<Node>();
        myClients = new LinkedList<TCPClient>();
        unStarted = true;
    }

    public void startListening (int port) {

        myServer = new AcceptConnection(port, this);
        new Thread(myServer).start();
    }

    public void requestJoin (String ownIP, String ownPort, String ip, String port) {
        TCPClient client = new TCPClient(ip, Integer.parseInt(port), NetworkCodes.TIMEOUT);

        String outType = "java.lang.String";
        String outObj = Integer.toString(NetworkCodes.JOIN) + " " + ownIP + " " + ownPort;

        client.sendObjectToServer(outType, outObj);

        myServer = new AcceptConnection(Integer.parseInt(ownPort), this);
        new Thread(myServer).start();
    }

    @Override
    public List<Node> getNodes () {
        return myNodes;
    }

    public synchronized void register (String iP, String port) {
        myNodes.add(new Node(iP, port));
    }

    @Override
    public boolean blockUntilNextAnswer () {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getNodeListSize () {
        return myNodes.size();
    }

    @Override
    public void sendToNode (int index, String word) {
        if (unStarted) {
            unStarted = false;
            initThreads();
        }
        
        String outType = "java.lang.String";

        myClients.get(index).sendObjectToServerThread(outType, word);

    }
    
    private void initThreads(){
        
        for (int i=0;i<myNodes.size();i++){
            Node n = myNodes.get(i);
            TCPClient client = new TCPClient(n.getIp(), n.getPort(), NetworkCodes.TIMEOUT);
            new Thread(client).start();
            myClients.add(client);
        }
        
    }
}
