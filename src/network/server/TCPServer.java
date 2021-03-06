package network.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import output.Distributor;
import reduce.Reducer;
import reduce.TeraSortReducer;
import reduce.WordCountReducer;
import map.Mapper;
import map.TeraSortMapper;
import map.WordCountMapper;
import network.NetworkCodes;
import network.NetworkMaster;
import keyvaluepair.KeyValuePair;


public class TCPServer {

    private class DealWithConnection implements Runnable{

        Socket myClientSocket;
        ObjectInputStream in;
        
        public DealWithConnection(Socket clientSocket){
            myClientSocket = clientSocket;
            try {
                in = new ObjectInputStream(myClientSocket.getInputStream());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run () {
            
            boolean eof = false;
            while(!eof){
                try{
                    
                    String inType = (String) in.readObject();
                    Object inObj = in.readObject();
                    eof = dealWithObjectReceived(inType, inObj);
    
                    
                } catch (Exception e){
                    e.printStackTrace();
                    return;
                }
            }

            try {
                myClientSocket.close();
            }
            catch (IOException e) {
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
    private static Mapper myCurrentMapper;
    private static Reducer<String, Integer> myCurrentReducer;
    private String receivedFile = null;
    private NetworkMaster myNetwork;
    
    
    public TCPServer(int port){
        PORT = port;
    }

    public void registerNetwork(NetworkMaster networkMaster){
        myNetwork = networkMaster;
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
            //System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked"})
    private boolean dealWithObjectReceived (String inType, Object inObj) {
        if (inType.equals("textfile")) {
            writeReceivedFile(inObj);
        }
        else {
            Class c = null;
            try {
                c = Class.forName(inType);
            }
            catch (ClassNotFoundException e) {
                //System.out.println("Client's object type is not found...");
                return true;
            }
            Object rvdObj = c.cast(inObj);
//            System.out.println("I received object \"" + c.cast(inObj) + "\" from the client!");

            if (inType.equals("java.lang.String")){
                String s = (String) rvdObj;
                
                if (s.startsWith(Integer.toString(NetworkCodes.JOIN))){ // what if a word starts with this??
                    String[] splits = s.split(" ");
                    myNetwork.register(splits[1], splits[2]);
                } 
                
                else if(s.startsWith(Integer.toString(NetworkCodes.UPDATENODES))) {
                    String[] splits = s.split(" ");
                    myNetwork.updateNodeList(splits[1]);
                } 
                
                else if (s.startsWith(Integer.toString(NetworkCodes.WORDCOUNT))) {
                	myCurrentMapper = new WordCountMapper(myNetwork);
                	myCurrentReducer = new WordCountReducer(myNetwork);
//                	myCurrentPostProcessor = new WordCountPostProcess(myNetwork);
//                	if (myCurrentPostProcessor == null) System.out.println("POST PROCESSOR STILL NULL!");
                } 
                else if (s.startsWith(Integer.toString(NetworkCodes.TERASORT))) {
                	myCurrentMapper = new TeraSortMapper(myNetwork);
                	myCurrentReducer = new TeraSortReducer(myNetwork);
//                	myCurrentPostProcessor = new WordCountPostProcess(myNetwork);
                } 
                else if (s.startsWith(Integer.toString(NetworkCodes.TERASORTSPLITER))) {
                	((TeraSortMapper) myCurrentMapper).receiveSpliter(s);
                }

                else if (s.startsWith(Integer.toString(NetworkCodes.REDUCEEOF))) {
                	String[] splits = s.split(" ");
                	myCurrentReducer.receiveEOF(Integer.parseInt(splits[1]), Integer.parseInt(splits[2]));
                	return true;
                }
                
                else if (s.startsWith(Integer.toString(NetworkCodes.MAPEOF))) {
                    String[] ss = s.split(" ");
                    myCurrentMapper.receiveEOF(Integer.parseInt(ss[1]));
                    return true;
                }
                else {	//receive map work
                    //System.out.println("Received msg to be mapped: " + s);
                    Distributor output = new Distributor(myNetwork);
                   	output.setMapper(myCurrentMapper);
                    myCurrentMapper.map(s, output);
                    myCurrentMapper.jobDoneCount();
                }
            }
            
            else if (inType.equals("keyvaluepair.KeyValuePair")){
                KeyValuePair<String, Integer> kvp = (KeyValuePair<String, Integer>) inObj;
                if (myNetwork.getIsHost()) {	//receiving end result
                    myNetwork.postProcess(kvp);
                } else {	//receiving reduce work
                    myCurrentReducer.addKVP(kvp);
                    myCurrentReducer.jobDoneCount();
                    //System.out.println("Reducing KVP: " + kvp.getKey().toString() + "," +kvp.getValue().intValue());   
                }
            }
        }
        return false;
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
            //System.out.println("Error reading client's file input or writing it to a file...");
            return;
        }

        //System.out.println("I received file \"" + DEFAULT_RECEIVED_FILE + "\" from the client!");
    }

    public String getMostRecentFileName () {
        return receivedFile;
    }
}
