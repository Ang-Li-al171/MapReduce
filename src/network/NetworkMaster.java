package network;

import java.util.LinkedList;
import java.util.List;
import keyvaluepair.KeyValuePair;
import network.client.TCPClient;
import output.OutputCollector;


public class NetworkMaster {

    private List<Node> myNodes;
    private OutputCollector<String, Integer> myOutput;
    private AcceptConnection myServer;
    private List<TCPClient> myClients;
    private boolean isHost;

    public NetworkMaster (OutputCollector<String, Integer> o) {
        myOutput = o;
        myNodes = new LinkedList<Node>();
        myClients = new LinkedList<TCPClient>();
        isHost = false;
    }

    public void startListening (int port) {

        isHost = true;
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
            c.quitClientThread();
        }
        myClients.clear();
        
        System.out.println("Cleared Node List...");
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

    public void sendKVPToNode (int index, KeyValuePair<String, Integer> kvp) {

        String outType = "keyvaluepair.KeyValuePair";

        myClients.get(index).sendObjToServerNonBlock(outType, kvp);

        System.out.println("Resent word key-value pair to reducer machine " + index);

    }

    public void collectKVP (KeyValuePair<String, Integer> kvp) {
        myOutput.collect(kvp);
        System.out.println("Collected key "+kvp.getKey()+", value " + kvp.getValue().toString());
    }

    private void initNewClient (Node n) {
        TCPClient client = new TCPClient(n.getIp(), n.getPort(), NetworkCodes.TIMEOUT);
        new Thread(client).start();
        myClients.add(client);
    }
}
