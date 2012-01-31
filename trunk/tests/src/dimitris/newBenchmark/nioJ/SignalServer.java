
import java.io.*;
import java.net.*;

public class SignalServer {

  //public static final int QUIT = 0x00000100;

  private int port;

  public SignalServer(int port) {
    this.port = port;
  }

  public void run() {
    ObjectInputStream in;
    boolean quit = false;

    try {
      ServerSocket serverSocket = new ServerSocket(port);
      while (!quit) {
        Socket clientSocket = serverSocket.accept();

        in = new ObjectInputStream(clientSocket.getInputStream());
        int x = in.readInt();

        if ((x & MyObject.BEGIN_TIMING) != 0)
          Server.sendTiming();
        if ((x & MyObject.BEGIN_COUNTING) != 0)
          Server.sendCounting();
        if ((x & MyObject.KILL_LOAD) != 0) {
          Server.sendKill();
          quit = true;
        }
      }
    }
    catch(Exception e){e.printStackTrace();}
    //System.out.println("Signal finished");
  }

  public static void main(String args[]) {
    new SignalServer(2020).run();
  }

}
