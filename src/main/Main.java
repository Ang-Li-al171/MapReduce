package main;

import java.io.FileNotFoundException;
import java.util.*;
import input.FileReader;

public class Main
{
  public static void main(String[] args)
  {
    //new WordCount().run();
      String file = args[0];
      System.out.println(file);
      List<String> nodeList = new ArrayList<>();
      for (int i=1;i<args.length;i++) {
          String ip = args[i];
          System.out.println(ip);    
          nodeList.add(ip);
      }
      FileReader fr = new FileReader(nodeList);
      try {
          fr.read("test.txt");
        }
        catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }
}