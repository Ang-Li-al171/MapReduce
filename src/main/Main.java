package main;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * Import format should be: filename ip1:port1 ip2:port2 ip3:port3
 * ie. test.txt 127.0.0.1:3000 127.0.0.1:3001 127.0.0.1:3002
 * 
 * @author weideng
 *
 */
public class Main
{
    public static void main (String[] args) throws FileNotFoundException
    {      
    NaiveWordCount counter = new NaiveWordCount();
    counter.countWords("test.txt");
    counter.printWordCount();
//        EndSystem myEndSystem = new EndSystem();
//
//        Scanner in = new Scanner(System.in);
//
//        System.out.println("Please enter command 'host' or 'join': ");
//
//        while(true){
//            String action = in.next();
//            if (action.equals("host")) {
//                System.out.println("Please enter a port number: ");
//                String port = in.next();
//                myEndSystem.hostNetwork(port);
//                System.out.printf("Hosting a network at port %d!\n", Integer.parseInt(port));
//                break;
//            }
//            else if (action.startsWith("join")) {
//                myEndSystem.joinHost(in.next(), in.next(), in.next(), in.next());
//                System.out.println("JOIN request sent to network host!");
//                break;
//            }
//            else {
//                System.out.println("Unrecognized action... Error!");
//            }
//        }
//
//        while (true) {
//        // only runs once for now, add the loop later
//        
//            System.out.println("Please enter the next command: mrWordCount/more commands coming");
//            String action2 = in.next();
//
//            if (action2.equals("mrWordCount")){
//                String file = in.next();
//                System.out.println("Please enter a file name: ");
//                System.out.println("Carrying out word count on file " + file);
//                
//                myEndSystem.runWordCount(file);
//                break;
//            }
//            
//        }
//        in.close();
    }
}
