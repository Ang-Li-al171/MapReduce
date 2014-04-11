package network;

import network.server.TCPServer;

public class AcceptConnection implements Runnable{
    
    TCPServer myServer;
    
    public AcceptConnection(int serverPort, NetworkMaster networkMaster){
        myServer = new TCPServer(serverPort);
        myServer.registerNetwork(networkMaster);
    }
    
    @Override
    public void run () {
        myServer.runServer();
    }

}
