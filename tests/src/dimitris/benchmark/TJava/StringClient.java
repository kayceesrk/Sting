import java.io.*;
import java.net.*;

import java.util.Random;
import java.util.concurrent.*;

public class StringClient implements Client {

  private static Random generator = new Random(System.currentTimeMillis());
  private String requestString;

  public StringClient() {
    requestString = new String("Number " + (generator.nextInt() % 1024) + " is beeing send");
  }

  public String client(String domain, int port) {
    String x = null;
    Socket clientSocket = null;      
    try {
      clientSocket = new Socket(domain, port);
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

      out.println("String");
      out.println(this.requestString);
      x = in.readUTF();
      clientSocket.close();
    }
    catch (IOException e) {e.printStackTrace();}
    return x;
  }

  public static void main(String []args) {
    System.out.println(new StringClient().client("", 2000));
  }
}

