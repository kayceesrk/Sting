import java.io.*;
import java.net.*;

import java.util.concurrent.*;

public class SimpleClient implements Client {

  public String client(String domain, int port, long [][]times, int i, int j) {
    int x = 0;
    Socket clientSocket = null;      
    try {
      clientSocket = new Socket(domain, port);
      times[i][j] = System.nanoTime();
      DataInputStream in = new DataInputStream(clientSocket.getInputStream());
      x = in.readInt();
      times[i][j] = System.nanoTime() - times[i][j];
      clientSocket.close();
    }
    catch (IOException e) {e.printStackTrace();}	
    
    return "" + x;
  } 

}

