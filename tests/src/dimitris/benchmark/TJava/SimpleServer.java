import java.io.*;
import java.net.*;

import java.util.concurrent.*;


public class SimpleServer implements Server{

  private ExecutorService es;

  public SimpleServer(int numThreads) {
    es = Executors.newFixedThreadPool(numThreads);
  }

  class socketWorker implements Runnable {
    Socket clientSocket;

    socketWorker(Socket s) {
      clientSocket = s;
    }

    public void run(){
      DataOutputStream out;
      try {
        out = new DataOutputStream(clientSocket.getOutputStream());
        out.writeInt(5);
      }
      catch (Exception e){}
    }
  }

  public void server(int port, int numClients) {
    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(port);
      while(numClients-- != 0){
        Socket clientSocket = serverSocket.accept();
        socketWorker w = new socketWorker(clientSocket);
        es.execute(w);
      }
    }
    catch (IOException e) {
      System.out.println("Accept failed: " + port);
      e.printStackTrace();
      System.exit(-1);
    }
    es.shutdown();
  }

}
