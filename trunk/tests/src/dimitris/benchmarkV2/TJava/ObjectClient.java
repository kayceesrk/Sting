import java.io.*;
import java.net.*;

import java.util.Random;
import java.util.concurrent.*;

public class ObjectClient implements Client {

  private static Random generator = new Random(System.currentTimeMillis());
  private String requestString;

  public ObjectClient() {
    requestString = new String("Number " + (generator.nextInt() % 1024) + " is beeing send");
  }

  public String client(String domain, int port, long [][]times, int i, int j) {
    Object x = null;
    Socket clientSocket = null;      
    try {
      clientSocket = new Socket(domain, port);
      times[i][j] = System.nanoTime();
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

      out.println("Object");
      out.println(this.requestString);
      try {
        x = in.readObject();
      } catch (Exception e) {}
      times[i][j] = System.nanoTime() - times[i][j];
      clientSocket.close();
    }
    catch (IOException e) {e.printStackTrace();}
    return (String) x;
  }

  public static void main(String []args) {
    long  [][]t = new long[1][1];
    System.out.println(new ObjectClient().client("", 2000, t, 0, 0));
  }
}

