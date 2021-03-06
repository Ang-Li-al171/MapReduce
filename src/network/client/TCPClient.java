package network.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class TCPClient implements Runnable{

    private final String HOSTIP;
    private final int PORT;
    private final int TIMEOUT;
    private Socket s;
    private List<Object> objToSend;
    private List<String> objType;
    private boolean end;
    private boolean connected;
    private ObjectOutputStream out;

    public TCPClient (String hostIp, int portNum, int timeOut) {
        HOSTIP = hostIp;
        PORT = portNum;
        TIMEOUT = timeOut;
        objToSend = new LinkedList<Object>();
        objType = new LinkedList<String>();
        end = false;
        connected = false;
    }

    public void sendObjectToServer(String outType, Object outObj){
        try {
            createSocketAndSend(outType, outObj);
        }
        catch (Exception e) {
            System.out.println("Something went wrong trying to send the object...");
            e.printStackTrace();
        }
    }
    
    public synchronized void sendObjToServerNonBlock (String outType, Object outObj) {
        objType.add(outType);
        objToSend.add(outObj);
        notifyAll();
    }
    
    public void sendFileToServer (String filePath) {

        try {
            Scanner fin = new Scanner(new FileInputStream(filePath));
            List<String> fileText = new ArrayList<String>();
            while (fin.hasNext()) {
                fileText.add(fin.nextLine());
            }
            fin.close();
            
            createSocketAndSend("textfile", fileText);
        }
        catch (Exception e) {
            System.out.println("Something went wrong trying to send the file...");
            e.printStackTrace();
        }

    }

    private void createSocketAndSend (String outType, Object outObj) throws IOException,
                                                                    ClassNotFoundException {

        if (!connected){
            connected = true;
            s = new Socket();
            s.connect(new InetSocketAddress(HOSTIP, PORT), TIMEOUT);
            out = new ObjectOutputStream(s.getOutputStream());
        }
        out.writeObject(outType);
        out.writeObject(outObj);
//        out.flush();

//        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
//        String message = (String) in.readObject();
//        System.out.println(message);
//
//        s.close();
    }

    public synchronized void quitClientThread() throws IOException{
        end = true;
        connected = false;
        if (s != null) s.close();
        notifyAll();
    }
    
    @Override
    public synchronized void run () {
        while(!end){
            while(objToSend.size() > 0){
                try {
                    //System.out.println("Current msg to send " + objToSend.get(0).toString() + "size of list " + objToSend.size());
                    createSocketAndSend(objType.get(0), objToSend.get(0));
                    objToSend.remove(0);
                    objType.remove(0);
                    //System.out.println("Current size: " + objToSend.size());
                }
                catch (Exception e) {
                    System.out.println("Something went wrong trying to send the object..." + HOSTIP + PORT);
                    return;
                }
            }
            while (objToSend.size() == 0) {
                try {
                    wait();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
