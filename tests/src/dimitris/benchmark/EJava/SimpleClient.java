import java.nio.channels.*;
import java.io.*;
import java.net.*;
import java.util.*;

import java.nio.*;
import java.nio.charset.Charset;

public class SimpleClient implements Client {

  private static Charset charset = Charset.forName("UTF-8");

  private static int byteArrayToInt(byte []b) {
    int x = 0;
    x |= b[0]; x <<= 8;
    x |= b[1]; x <<= 8;
    x |= b[2]; x <<= 8;
    x |= b[3];
    return x;
  }

  public String client (String domain, int port) {
    int x = 0;
    ByteBuffer b = ByteBuffer.allocate(64);

    try {
      InetSocketAddress socketAddress = new InetSocketAddress(domain, port);
      SocketChannel sc = SocketChannel.open(socketAddress);

      int numRead = 0; 

      while(numRead < 4) {
        numRead += sc.read(b);
      }

      b.flip();

      x = byteArrayToInt(b.array());
      sc.close();
    }
    catch (IOException e) {e.printStackTrace();}
    return "" + x;

  }

    public static void main(String[] args) throws IOException {
      new SimpleClient().client("", 1234);
    }
}

