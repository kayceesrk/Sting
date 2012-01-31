import java.io.*;
import java.net.*;

import java.util.Random;
import java.util.concurrent.*;

public class IntClient implements Client {

  private static Random generator = new Random(System.currentTimeMillis());
  private String requestString;

  public IntClient() {
    requestString = new String("Number " + (generator.nextInt() % 1024) + " is beeing send");
  }

  public String client(String domain, int port) {
    int x = 0;
    Socket clientSocket = null;      
    try {
      clientSocket = new Socket(domain, port);
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      //DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

      out.println("int");
      out.println(this.requestString);
      x = in.readInt();
      clientSocket.close();
    }
    catch (IOException e) {e.printStackTrace();}
    return "" + x;
  }

  public static void main(String []args) {
    System.out.println(new IntClient().client("", 2000));
  } 

}

