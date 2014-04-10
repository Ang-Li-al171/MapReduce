package main;
import java.util.*;


public class Main
{
    public static void main (String[] args)
    {
        WordCount myMain = new WordCount();

        Scanner in = new Scanner(System.in);

        System.out.println("Please enter command host or join: ");

        while(true){
            String action = in.next();
            if (action.equals("host")) {
                String port = in.next();
                myMain.hostNetwork(port);
                System.out.printf("Hosting a network at port %d!\n", Integer.parseInt(port));
                break;
            }
            else if (action.startsWith("join")) {
                myMain.joinHost(in.next(), in.next(), in.next(), in.next());
                System.out.println("JOIN request sent to network host!");
                break;
            }
            else {
                System.out.println("Unrecognized action... Error!");
            }
        }

        while (true) {
        // only runs once for now, add the loop later
        
            System.out.println("Please enter the next command: mrWordCount/more commands coming");
            String action2 = in.next();

            if (action2.equals("mrWordCount")){
                String file = in.next();
                System.out.println("Carrying out word count on file " + file);
                
                myMain.runWordCount(file);
                break;
            }
            
        }
        in.close();
    }
}
