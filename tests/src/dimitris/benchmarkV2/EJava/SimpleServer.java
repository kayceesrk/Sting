import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.nio.*;
import java.nio.charset.Charset;

public class SimpleServer implements Server {

  private static Charset charset = Charset.forName("UTF-8");

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
    SocketChannel sc;

    ByteBuffer b = ByteBuffer.allocate(64);
    
    
    try {
        sel = Selector.open();

        ss = ServerSocketChannel.open();
        ss.configureBlocking(false);
        ss.socket().bind(new InetSocketAddress(port));
    
        ss.register(sel, SelectionKey.OP_ACCEPT);        
    } catch (IOException e) {}

    while (numClients-- != 0) {
      try {
        sel.select();
        // Get list of selection keys with pending events
        Iterator it = sel.selectedKeys().iterator();

        while (it.hasNext()) {
          SelectionKey selKey = (SelectionKey) it.next();
          it.remove();

          if (selKey.isAcceptable()) {
            ss = (ServerSocketChannel) selKey.channel();
            sc = ss.accept();

            b.clear();
            b.put(intToByteArray(5));
            b.flip();

            sc.write(b);

            sc.close();
          }
        }
      } catch (Exception e) {e.printStackTrace();}
    }
  }

  public static void main(String args[]) throws Exception{
    new SimpleServer().server(1234, 1234);
  }

}
