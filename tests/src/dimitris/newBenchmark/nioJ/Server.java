import java.nio.*;

import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable {
  private int port;
  private int numClients;
  private long count = 0;
  private static long t = 0;

  private static int signal = MyObject.NO_SIGNAL;

  public static final int REC = 1;
  public static final int QUIT = 2;

  private static boolean counting = false;

  public Server(int port, int numClients) {
    this.port = port;
    this.numClients = numClients;
  }

  private int byteArrayToInt(byte[] b) {
    int x = 0;
    x |= b[0]; x<<=8;
    x |= b[1]; x<<=8;
    x |= b[2]; x<<=8;
    x |= b[3];
    return x;
  }

  public void run() {
    Selector sel = null;
    ServerSocketChannel ss = null;
    SocketChannel sc = null;
    Socket s = null;
    ReadObject ro = null;
    byte [] ba = null;
    int branch;

    try {
      sel = Selector.open();

      ss = ServerSocketChannel.open();
      ss.configureBlocking(false);
      ss.socket().bind(new InetSocketAddress(port));

      ss.register(sel, SelectionKey.OP_ACCEPT);        
    } catch (IOException e) {e.printStackTrace();}

    while (numClients != 0) {
      try {
        sel.select();
        Iterator it = sel.selectedKeys().iterator();

        while (it.hasNext()) {
          SelectionKey selKey = (SelectionKey)it.next();
          it.remove();

          if (selKey.isAcceptable()) {
            ss = (ServerSocketChannel)selKey.channel();
            sc = ss.accept();
            sc.configureBlocking(false);
            
            sc.register(sel, SelectionKey.OP_READ).attach(new ReadObject(8192));
          }
          else if (selKey.isReadable()) {
            sc = (SocketChannel)selKey.channel();

            ro = (ReadObject) selKey.attachment();
	          ro.numRead = sc.read(ro.b);

	          if (ro.numRead == -1) {
              numClients--;
	  	        selKey.cancel();
		          sc.close();
	          }
//System.out.println("debug1 " + ro.numRead);
            if (ro.numRead == 4) {
//System.out.println("debug2");
              ro.numRead = 0;
              ro.b.flip();
              ba = ro.b.array();
              branch = byteArrayToInt(ba);

              if(branch == REC) {
                ro.b.clear();
                ba = new byte[MyObject.DEFAULT_SIZE];
                ba[0] = 0;
                ba[0] |= signal;
                ro.b.put(ba);
                ro.b.flip();
                sc.write(ro.b);
                if (counting) {
                  count++;
//                  System.out.println(count);
                }
                ro.b.clear();
                selKey.attach(ro);
              }
              else if (branch == QUIT) {
                numClients--;
 //               System.out.println(numClients);
                selKey.cancel();
        	      sc.close();
              }
            }
            else {
              selKey.attach(ro);
            }
            
          }
        }
      } catch(Exception e) {e.printStackTrace();}
    }
    t = System.nanoTime() - t;
    System.out.println("Throughput Count: " + count + ". Time: " + t);

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
