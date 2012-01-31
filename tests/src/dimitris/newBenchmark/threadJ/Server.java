
import java.io.*;
import java.net.*;

public class Server implements Runnable {
  private int port;
  private int numClients;
  private long count[];
  private boolean finished[];

  private static int signal = MyObject.NO_SIGNAL;

  private static long t = 0;
  public static final int REC = 1;
  public static final int QUIT = 2;

  private static boolean counting = false;

  private int clients;

  public Server(int port, int numClients) {
    this.port = port;
    this.clients = this.numClients = numClients;
    this.count = new long[numClients];
    this.finished = new boolean[numClients];

    for (int i = 0; i < numClients; i++) {
      count[i] = 0;
      finished[i] = false;
    }
  }

  class socketWorker {

    public void run(Socket clientSocket, /*long count[],*/ int index){
      ObjectOutputStream out;
      ObjectInputStream in;
      int branch;

      try {
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
      
        while(true) {
          branch = in.readInt();

          if(branch == REC) {
            out.writeObject(new MyObject(signal));
            out.flush();          
            if (counting) {
              count[index]++;
//          System.out.println(count);
            }
          }
          else if (branch == QUIT) {
            finished[index] = true;
            clientSocket.close();
            break;
          }
        }
      }
      catch(Exception e) {e.printStackTrace();}
    }
  }

  public void run() {
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(port);
      while(numClients-- != 0) {
        Socket clientSocket = serverSocket.accept();
        final Socket c = clientSocket;
        final int index = numClients;
        new Thread() {
          public void run() {
            new socketWorker().run(c, /*count,*/ index);
          } 
        }.start();
      }
    }
    catch (IOException e) {e.printStackTrace();}
    
    // check for termination and count
    for(boolean f = false; !f;) {
      f = true;
      for (int i = 0; i < clients; i++) {
        f = f && finished[i];
      }
    }

    t = System.nanoTime() - t;

    int totalCount;
    for (int i = totalCount = 0; i < clients; i++) {
      totalCount += count[i];
    }
    System.out.println("Throughput Count: " + totalCount + ". Time: " + t);
  }

  public static void sendKill() {
    signal |= MyObject.KILL_LOAD;
  }

  public static void sendTiming() {
    signal |= MyObject.BEGIN_TIMING;
  }

  public static void sendCounting() {
    t = System.nanoTime();
    counting = true;
  }


}
