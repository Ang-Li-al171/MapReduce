package main;

import java.io.FileNotFoundException;
import java.util.*;
import input.FileReader;
import network.*;
import input.*;

public class Main
{
  public static void main(String[] args)
  {
      WordCount myMain = new WordCount();
      
      Scanner in = new Scanner(System.in);
      
      System.out.println("Please enter command host or join: ");
      String action = in.next();
      
      if (action.equals("host")){
          String port = in.next();
          myMain.hostNetwork(port);
          System.out.printf("Hosting a network at port %d!\n", Integer.parseInt(port));
      } else if (action.startsWith("join")){
          myMain.joinHost(in.next(), in.next(), in.next(), in.next());
          System.out.println("Request sent to join a network!");
      }
      else {
          System.out.println("Unrecognized action... Error!");
      }
      
      System.out.println("Please enter the next command: ");
      String a = in.next();
      
      in.close();
      
      String file = args[0];
      System.out.println(file);
      List<Node> nodeList = new ArrayList<>();
      for (int i=1;i<args.length;i++) {
          String split[] = args[i].split(":"); 
          String ip = split[0];
          String port = split[1];   
          nodeList.add(new Node(ip, port));
      }
      Splitter s = new Splitter(nodeList); //Programmer can customize the splitter they use
      FileReader fr = new FileReader(s); //Programmer can also customize the reader they use
      try {
          fr.read("test.txt");
        }
        catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
}