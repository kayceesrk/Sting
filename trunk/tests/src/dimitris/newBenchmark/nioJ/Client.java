
import java.nio.*;

import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;


public class Client {

  public static int port = 2000;
  public static String host = "";

  private boolean timing;
  private int iterations;

  /*Use to simulate begin timing and kill load signals*/
  public boolean beginTiming;
  public boolean killLoad;

  private int clientNum;
  private long []times;

  /*Load Client*/
  public Client(int clientNum) {
    this.timing = false;
    this.beginTiming = false;
    this.killLoad = false;
    this.iterations = 0;  
    this.clientNum = clientNum;
  }
  
  /*Time Client*/
  public Client(int clientNum, int iterations) {
    this.timing = true;
    this.beginTiming = false;
    this.killLoad = false;
    this.iterations = iterations;
    this.times = new long[iterations];
    this.clientNum = clientNum;
  }


  private static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
  }

  public void client() {

    ByteBuffer b = ByteBuffer.allocate(8192);
    int numRead = 0;
    int signal = 0;
    int i = 0;
    try {
      InetSocketAddress socketAddress = new InetSocketAddress(host, port);
      SocketChannel sc = SocketChannel.open(socketAddress);
      sc.configureBlocking(false);

      while (true) {
        if(!this.killLoad) {
          if (this.beginTiming && this.timing && (i < iterations)) {
            times[i] = System.nanoTime();
          }
          b.clear();
          b.put(intToByteArray(Server.REC));
          b.flip();
          sc.write(b);

          b.clear();
          numRead = 0;
          while(numRead < MyObject.DEFAULT_SIZE) {
            numRead += sc.read(b);
          }
          signal = 0;
          signal |= b.array()[0];

          killLoad = ((signal & MyObject.KILL_LOAD) != 0);
          beginTiming = ((signal & MyObject.BEGIN_TIMING) != 0);
       //   System.out.println(clientNum + ":" + killLoad + ":" + beginTiming);

          if (this.beginTiming && this.timing && (i < iterations)) {
            times[i] = System.nanoTime() - times[i];
            i++;
          }
        }
        else {
          b.clear();
          b.put(intToByteArray(Server.QUIT));
          b.flip();
        //  System.out.println(clientNum + " sends quit");
          break;
        }
      }
      sc.close();
    }
    catch (IOException e) {e.printStackTrace();}
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
