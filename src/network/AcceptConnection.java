package network;

import network.server.TCPServer;

public class AcceptConnection implements Runnable{
    
    TCPServer myServer;
    
    public AcceptConnection(int serverPort, NetworkMaster network){
        myServer = new TCPServer(serverPort);
        myServer.registerNetwork(network);
    }
    
    @Override
    public void run () {
        myServer.runServer();
    }

}
