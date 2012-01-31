import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.nio.*;
import java.nio.charset.Charset;

public class RequestServer implements Server {

  private int throughput = 0;

  private long start, end;

  private static Charset charset = Charset.forName("UTF-8");

  private int parse(String str){
      int numEnd = str.indexOf(' ', 7);
      String sub = str.substring(7, numEnd);
      return Integer.parseInt(sub);
  }


  /*Converts an int to byte array*/
  private static final byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
  }


  public void server(int port, int numClients) {

    Selector sel = null;
    ServerSocketChannel ss = null;
    SocketChannel sc = null;
    Socket s = null;
    ByteBuffer b = null;

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
            
            sc.register(sel, SelectionKey.OP_READ).attach(ByteBuffer.allocate(4096));
          }
          else if (selKey.isReadable()) {
            sc = (SocketChannel)selKey.channel();

            b = (ByteBuffer) selKey.attachment();
	          int numRead = sc.read(b);

	          if (numRead == -1) {
	  	        selKey.cancel();
		          sc.close();
	          }
            
            int i;

            b.flip();
            CharBuffer cb = charset.decode(b);
  
            if (cb.charAt(cb.length() - 1) == '@') {
              int x = parse(cb.toString());
              b.clear();
              b.put(intToByteArray(x));
              b.flip();
              sc.write(b);
              selKey.cancel();
      	      sc.close();
              numClients--;
            }
            else {
              b.clear();
              b.put(charset.encode(cb));
              selKey.attach(b);
            }
            
          }
        }
      } catch(Exception e) {e.printStackTrace();}
    }
  }

  public static void main(String args[]) throws Exception{
    new RequestServer().server(1234, 1234);
  }

}
