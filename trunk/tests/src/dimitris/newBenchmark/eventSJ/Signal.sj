import java.io.*;
import java.net.*;

public class Signal {

  public static void sendSignal(String host, int port, int signal) {
    try {
      Socket clientSocket = new Socket(host, port);
      ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
      out.writeInt(signal);
      out.flush();
      clientSocket.close();
    }
    catch (Exception e){e.printStackTrace();}
  }

  public static void main(String args[]) {
    if (args.length < 3) {
      System.out.println("Usage: java Sigal <host> <port> <signal1> [signal2] [signal3]");
      return;
    }

    int signal = 0;
    for (int i = 2; i < args.length; i++) {
      if (args[i].equals("Kill"))
        signal |= MyObject.KILL_LOAD;
      else if (args[i].equals("Time"))
        signal |= MyObject.BEGIN_TIMING;
      if (args[i].equals("Count"))
        signal |= MyObject.BEGIN_COUNTING;
    }

    sendSignal(args[0], Integer.parseInt(args[1]), signal);

  }

}
