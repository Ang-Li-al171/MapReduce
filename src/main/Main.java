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
public class Main {
	private static Scanner in = new Scanner(System.in);
	private static String[] validTasksArray = {"mrwordcount", "mrterasort"};
	private static Set<String> validTasks = new HashSet<String>(Arrays.asList(validTasksArray));
	
    public static void main (String[] args) {      
        EndSystem myEndSystem = new EndSystem();

        System.out.println("Please enter command 'host' or 'join': ");

        while(true){
            String action = in.next();
            if (action.equals("host")) {
                System.out.println("Please enter a port number: ");
                String port = in.next();
                myEndSystem.hostNetwork(port);
                System.out.printf("Hosting a network at port %d!\n", Integer.parseInt(port));
                break;
            }
            else if (action.startsWith("join")) {
                System.out.println("Enter your ip:");
                String ownIp = in.next();
                System.out.println("Enter your port:");
                String ownPort = in.next();
                System.out.println("Enter host ip:");
                String hostIp = in.next();
                System.out.println("Enter host port:");
                String hostPort = in.next();      
                myEndSystem.joinHost(ownIp, ownPort, hostIp, hostPort);
                System.out.println("JOIN request sent to network host!");
                break;
            }
            else {
                System.out.println("Unrecognized action... Error!");
            }
        }    

        while (true && myEndSystem.isHost()) {
            System.out.println("Programs available:");
            System.out.println("\tmrWordCount (requires peers)");
            System.out.println("\tnaiveWordCount");
            System.out.println("\tmrTeraSort (requires peers)");
            System.out.println("Which program would you like to run?");
            String action2 = in.next().toLowerCase();
            
            if (validTasks.contains(action2)) {     
                if (!myEndSystem.hasPeers()) {
                    System.out.println("You need peer nodes to run this program!");
                    continue;
                }
            	System.out.println("Please enter an input file name: ");
                String file = in.next();
                myEndSystem.runTask(action2, file);
            } else if (action2.equals("naivewordcount")){  
                System.out.println("Please enter an input file name: ");
                String file = in.next();    
                System.out.println("Carrying out naive wordcount on file " + file);
                
                try {
                    runNaiveWC(file);
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                
            } else {
                System.out.println("Invalid command: " + action2);
            }
            
        }
    }
    
    public static void runNaiveWC(String file) throws FileNotFoundException {
        NaiveWordCount counter = new NaiveWordCount();
        long startTime = System.currentTimeMillis();
        counter.countWords(file);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        counter.printWordCount();
        System.out.println("Naive word count took "+duration+" milliseconds");    
    }
}
