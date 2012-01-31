import java.io.*;
import java.net.*;

import java.util.concurrent.*;

public class RequestServer implements Server{

  private ExecutorService es;

  public RequestServer(int numThreads) {
    es = Executors.newFixedThreadPool(numThreads);
  }

  class socketWorker implements Runnable {
    Socket clientSocket;

    private int parse(String str){
      int numEnd = str.indexOf(' ', 7);
      String subs = str.substring(7, numEnd);
      return Integer.valueOf(subs);
    }

    socketWorker(Socket s) {
      clientSocket = s;
    }

    public void run(){
      try {
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                               clientSocket.getInputStream()));
        String str = in.readLine();
        out.writeInt(parse(str));
      }
      catch (Exception e){
      }
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
    } catch (IOException e) {
      System.out.println("Accept failed: " + port);
      System.exit(-1);
    }
    es.shutdown();
  }

}
