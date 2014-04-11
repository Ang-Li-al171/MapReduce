package network.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import map.MapExecutor;
import network.NetworkCodes;
import network.NetworkMaster;
import keyvaluepair.KeyValuePair;


public class TCPServer {

    private class DealWithConnection implements Runnable{

        Socket myClientSocket;
        
        public DealWithConnection(Socket clientSocket){
            myClientSocket = clientSocket;
        }
        
        @Override
        public void run () {
            
            try{
                ObjectInputStream in = new ObjectInputStream(myClientSocket.getInputStream());
                String inType = (String) in.readObject();
                Object inObj = in.readObject();
                dealWithObjectReceived(inType, inObj);
                in.close();
                
//                ObjectOutputStream out = new ObjectOutputStream(myClientSocket.getOutputStream());
//                out.writeObject("Hi Client, this is server. Your information has been received");
//                out.flush();
//                out.close();

                myClientSocket.close();
                
            } catch (Exception e){
//                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        
    }
    
    private static int PORT;
    private static final String DEFAULT_RECEIVED_FILE = System.getProperty("user.dir") +
                                                        File.separator
                                                        + "src" + File.separator + "network" +
                                                        File.separator + "server" + File.separator +
                                                        "ReceivedFile.txt";
    private static MapExecutor myMapExecutor;
    private Object receivedObj = null;
    private String receivedFile = null;
    
    private NetworkMaster myNetwork;
    
    public TCPServer(int port){
        PORT = port;
        myMapExecutor = new MapExecutor();
    }

    public void registerNetwork(NetworkMaster networkMaster){
        myNetwork = networkMaster;
        myMapExecutor.registerNetwork(networkMaster);
    }
    
    @SuppressWarnings("resource")
    public void runServer () {
        try {

            ServerSocket serverS = new ServerSocket(PORT, 10);

            while (true) {

                Socket clientSocket = serverS.accept();

                new Thread(new DealWithConnection(clientSocket)).start();
                
            }
        }
        catch (Exception e) {
//            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void dealWithObjectReceived (String inType, Object inObj) {
        if (inType.equals("textfile")) {
            writeReceivedFile(inObj);
        }
        else {
            Class c = null;
            try {
                c = Class.forName(inType);
            }
            catch (ClassNotFoundException e) {
                System.out.println("Client's object type is not found...");
                return;
            }
            receivedObj = c.cast(inObj);
//            System.out.println("I received object \"" + c.cast(inObj) + "\" from the client!");

            // do whatever you want to do with the objects here
            // register the node into myNetwork if the action code matches
            if (inType.equals("java.lang.String")){
                String s = (String) receivedObj;
                
                if (s.startsWith(Integer.toString(NetworkCodes.JOIN))){ // what if a word starts with this??
                    String[] splits = s.split(" ");
                    myNetwork.register(splits[1], splits[2]);
                } 
                
                else if(s.startsWith(Integer.toString(NetworkCodes.UPDATENODES))) {
                    String[] splits = s.split(" ");
                    myNetwork.updateNodeList(splits[1]);
                } 
                
                else {
                    System.out.println("Received msg: " + s);
                    myMapExecutor.parseAndMap(s);
                }
            }
            
            else if (inType.equals("keyvaluepair.KeyValuePair")){
                KeyValuePair kvp = (KeyValuePair) inObj;
                myNetwork.collectKVP(kvp);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void writeReceivedFile (Object inObj) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(DEFAULT_RECEIVED_FILE));

            List<String> fileLines = (List<String>) inObj;
            for (String s : fileLines) {
                out.write(s + "\n");
            }

            out.close();
            receivedFile = DEFAULT_RECEIVED_FILE;
        }
        catch (Exception e) {
            System.out.println("Error reading client's file input or writing it to a file...");
            return;
        }

        System.out.println("I received file \"" + DEFAULT_RECEIVED_FILE + "\" from the client!");
    }

    public Object getMostRecentObject () {
        return receivedObj;
    }

    public String getMostRecentFileName () {
        return receivedFile;
    }
}
