package network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import keyvaluepair.KeyValuePair;
import network.client.TCPClient;
import output.Distributor;
import postprocess.PostProcess;


public class NetworkMaster<K, V> {

    private List<Node> myNodes;
    private Distributor<String, Integer> myOutput;
    private int myPort;
    private String myIp;
    private String myHostPort;
    private String myHostIp;
    private TCPClient hostTCPClient;
    private AcceptConnection myServer;
    private List<TCPClient> myClients;
    private boolean isHost;
    private long startTime;
    private static PostProcess myCurrentPostProcessor;

    public NetworkMaster (Distributor<String, Integer> o) {
        myOutput = o;
        myNodes = new LinkedList<Node>();
        myClients = new LinkedList<TCPClient>();
        isHost = false;
    }
    
    public NetworkMaster () {
        myNodes = new LinkedList<Node>();
        myClients = new LinkedList<TCPClient>();
        isHost = false;
    }

    public void startListening (int port) {
        isHost = true;
        myServer = new AcceptConnection(port, this);
        new Thread(myServer).start();
    }

    public void registerTimer(long startT){
        startTime = startT;
    }
    
    public long timeSpent(long currT){
        return currT-startTime;
    }
    
    public void requestJoin (String ownIP, String ownPort, String ip, String port) {
        
        myServer = new AcceptConnection(Integer.parseInt(ownPort), this);
        new Thread(myServer).start();
        
        myHostIp = ip;
        myIp = ownIP;
        myPort = Integer.parseInt(ownPort);
        myHostPort = port;
        hostTCPClient = new TCPClient(myHostIp, Integer.parseInt(myHostPort), NetworkCodes.TIMEOUT);

        String outType = "java.lang.String";
        String outObj = Integer.toString(NetworkCodes.JOIN) + " " + ownIP + " " + ownPort;

        hostTCPClient.sendObjectToServer(outType, outObj);

    }

    public List<Node> getNodes () {
        return myNodes;
    }

    public synchronized void register (String iP, String port) {
        myNodes.add(new Node(iP, port));
        initNewClient(myNodes.get(myNodes.size()-1));
        
        System.out.printf("Registered peer ip: %s, port: %s, into the network!\n", iP, port);

        if (isHost) {
            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(NetworkCodes.UPDATENODES));
            sb.append(" ");
            for (Node n : myNodes) {
                sb.append(n.getIp());
                sb.append(":");
                sb.append(Integer.toString(n.getPort()));
                sb.append(";");
            }
            sb.deleteCharAt(sb.length() - 1);

            for (int i = 0; i < myNodes.size(); i++) {
                sendMsgToNode(i, sb.toString());
            }
        }
    }

    public void updateNodeList (String listStr) {
        String[] peers = listStr.split(";");
        
        myNodes.clear();
        
        for(TCPClient c : myClients){
            try {
                c.quitClientThread();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        myClients.clear();
        
        //System.out.println("Cleared Node List...");
        for (String onePeer : peers) {
            String[] ss = onePeer.split(":");
            register(ss[0], ss[1]);
        }
    }

    public boolean blockUntilNextAnswer () {
        // TODO Auto-generated method stub
        return false;
    }

    public int getNodeListSize () {
        return myNodes.size();
    }

    public void sendMsgToNode (int index, String word) {

        String outType = "java.lang.String";

        myClients.get(index).sendObjToServerNonBlock(outType, word);

    }
    
    public void sendMsgToAll(String msg) {
    	for (TCPClient c: myClients) {
    		c.sendObjToServerNonBlock("java.lang.String", msg);
    	}
    }

    public void sendKVPToNode (int index, KeyValuePair<K, V> kvp) {

        String outType = "keyvaluepair.KeyValuePair";

        myClients.get(index).sendObjToServerNonBlock(outType, kvp);

        //System.out.println("Sent " + kvp.getKey() + " to reducer machine " + index);

    }
    
    public void sendKVPToPortAndIP (String IP, String port, KeyValuePair<K,V> kvp) {
    	hostTCPClient.sendObjectToServer("keyvaluepair.KeyValuePair", kvp);
    }
    
    public void sendMapEOFToAll(int[] counts){
        for(int i=0; i<myNodes.size(); i++){
            String t = Integer.toString(NetworkCodes.MAPEOF) + " " + counts[i];
            sendMsgToNode(i, t);
        }
    }
    
    public void sendReduceEOFToAll(int[] counts){
        
        for(int i=0; i<myNodes.size(); i++){

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toString(NetworkCodes.REDUCEEOF));
            sb.append(" ");
            sb.append(myPort);
            sb.append(" ");
            sb.append(Integer.toString(counts[i]));
            sendMsgToNode(i, sb.toString());
        }
    }
    
//    public void collectKVP (KeyValuePair<String, Integer> kvp) {
//        myOutput.collect(kvp);
//        System.out.println("Collected key "+kvp.getKey()+", value " + kvp.getValue().toString());
//    }

    private void initNewClient (Node n) {
        TCPClient client = new TCPClient(n.getIp(), n.getPort(), NetworkCodes.TIMEOUT);
        new Thread(client).start();
        myClients.add(client);
    }
    
    
    public void setPostProcessor(PostProcess p) {
    	myCurrentPostProcessor = p;
    }
    
    public void postProcess(KeyValuePair kvp){
    	myCurrentPostProcessor.receiveKVP(kvp);
    }
    
    public int getPort() {
    	return myPort;
    }
    
    public boolean getIsHost() {
    	return isHost;
    }
    
    public String getHostIP() {
    	return myHostIp;
    }
    
    public String getHostPort() {
    	return myHostPort;
    }
    
    public String getIP() {
    	return myIp;
    }
    
    public boolean isHost() {
        return isHost;
    }
}
