import java.io.*;
import java.net.*;

import java.util.concurrent.*;

public class SimpleClient implements Client {

  public String client(String domain, int port) {
    int x = 0;
    Socket clientSocket = null;      
    try {
      clientSocket = new Socket(domain, port);
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      x = in.readInt();
      clientSocket.close();
    }
    catch (IOException e) {e.printStackTrace();}	
    
    return "" + x;
  } 

}

