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
    public static void main (String[] args)
    {      
        EndSystem myEndSystem = new EndSystem();

        Scanner in = new Scanner(System.in);

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
                myEndSystem.joinHost(in.next(), in.next(), in.next(), in.next());
                System.out.println("JOIN request sent to network host!");
                break;
            }
            else {
                System.out.println("Unrecognized action... Error!");
            }
        }

        while (true) {
        // only runs once for now, add the loop later
        
            System.out.println("Please enter the next command: mrWordCount/naiveWordCount");
            String action2 = in.next().toLowerCase();

            if (action2.equals("mrwordcount")){
                System.out.println("Please enter a file name: ");
                String file = in.next();    
                System.out.println("Carrying out map-reduce wordcount on file " + file);
                
                myEndSystem.runTask("wordcount", file);
                
                break;
                
            } else if (action2.equals("mrterasort")){
            	System.out.println("Please enter a file name: ");
                String file = in.next();    
                
                System.out.println("Carrying out map-reduce terasort on file " + file);
                myEndSystem.runTeraSort(file);
            	
            	
            } else if (action2.equals("naivewordcount")){
                
                System.out.println("Please enter a file name: ");
                String file = in.next();    
                System.out.println("Carrying out naive wordcount on file " + file);
                
                try {
                    runNaiveWC();
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                
            } else {
                System.out.println("Invalid command: " + action2);
            }
            
        }
        in.close();
    }
    
    public static void runNaiveWC() throws FileNotFoundException {
        NaiveWordCount counter = new NaiveWordCount();
        long startTime = System.currentTimeMillis();
        counter.countWords("test.txt");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        counter.printWordCount();
        System.out.println("Naive word count took "+duration+" milliseconds");    
    }
}
