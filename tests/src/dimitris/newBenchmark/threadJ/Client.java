import java.io.*;
import java.net.*;

public class Client {

  public static int port = 2000;
  public static String host = "";
  protected boolean timing;
  protected int iterations;
  public boolean beginTiming;
  public boolean killLoad;
  protected int clientNum;
  protected long[] times;
  
  public static void setAddress(String host, int port) {
      Client.host = new String(host);
      Client.port = port;
  }
  
  public Client(int clientNum) {
      this.timing = false;
      this.beginTiming = false;
      this.killLoad = false;
      this.iterations = 0;
      this.clientNum = clientNum;
  }
    
  public Client(int clientNum, int iterations) {
      this.timing = true;
      this.beginTiming = false;
      this.killLoad = false;
      this.iterations = iterations;
      this.times = (new long[iterations]);
      this.clientNum = clientNum;
  }

  public void client() {
    Socket clientSocket = null;
    ObjectOutputStream out = null;
    ObjectInputStream in = null;
    boolean count = true;
    MyObject o = null;
    int i = 0;

    try {
      clientSocket = new Socket(host, port);
      in = new ObjectInputStream(clientSocket.getInputStream());
      out = new ObjectOutputStream(clientSocket.getOutputStream());
    
      while (true) {
        if(!this.killLoad) {
          if (this.beginTiming && this.timing && (i < iterations)) {
            times[i] = System.nanoTime();
          }
          out.writeInt(Server.REC);
          out.flush();

          o = (MyObject) in.readObject();

          killLoad = o.killSignal();
          beginTiming = o.timeSignal();

         //   System.out.println(clientNum + ":" + killLoad + ":" + beginTiming);

          if (this.beginTiming && this.timing && (i < iterations)) {
            times[i] = System.nanoTime() - times[i];
            i++;
          }
        }
        else {
          out.writeInt(Server.QUIT);
          out.flush();
        //  System.out.println(clientNum + " sends quit");
          break;
        }
      }
      clientSocket.close();
    }
    catch (Exception e) {e.printStackTrace();}
    if (timing) {
      for (int j = 0; j < iterations; j++)
        System.out.println("Client Number: " + clientNum + ".Iteration: " + j + ". Time: " + times[j] + ".");
    }
  }

  
  public static void main(String [] args) {

    if (args.length < 5) {
      System.out.println("Usage: sessionj ClientRunner <host> <port> <Timed Clients> <session length> <msg size>");
      return;
    }

    Client.host = args[0];
    Client.port = Integer.parseInt(args[1]);

    //int loadClients = Integer.parseInt(args[2]);
    int timedClients = Integer.parseInt(args[2]);
    final int iterations = Integer.parseInt(args[3]);
    MyObject.DEFAULT_SIZE = Integer.parseInt(args[4]);
    
    int i;

    for (i = 0; i < timedClients; i++)	{
      final int j = i;
      new Thread() {
        public void run() {
          new Client(j, iterations).client();
         // System.out.println(j + " finished");
        }
      }.start();
    }

  }

}
