package main;

import java.io.FileNotFoundException;
import java.util.*;
import input.FileReader;
import network.*;
import input.*;

/**
 * Import format should be: filename ip1:port1 ip2:port2 ip3:port3
 * ie. test.txt 127.0.0.1:3000 127.0.0.1:3001 127.0.0.1:3002
 * 
 * @author weideng
 *
 */
public class Main
{
  public static void main(String[] args)
  {
      new WordCount().run();
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