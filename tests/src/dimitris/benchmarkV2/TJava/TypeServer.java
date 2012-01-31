import java.io.*;
import java.net.*;

import java.util.concurrent.*;


public class TypeServer implements Server {

  private ExecutorService es;

  public TypeServer(int numThreads) {
    es = Executors.newFixedThreadPool(numThreads);
  }

  class socketWorker implements Runnable {
    Socket clientSocket;

    private int iparse(String str){
      int numEnd = str.indexOf(' ', 7);
      String subs = str.substring(7, numEnd);
      return Integer.parseInt(subs);
    }
   
    private String sparse(String str){
      int numEnd = str.indexOf(' ', 7);
      return str.substring(7, numEnd);
    }

   private Object oparse(String str){
     return (Object) new String(str);
   }

   socketWorker(Socket s) {
     clientSocket = s;
   }

    public void run(){
      try {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                                               clientSocket.getInputStream()));
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
        String str = in.readLine();
        String str2 = in.readLine();

        if (str.compareTo("int") == 0) {
          out.writeInt(iparse(str2));
          out.flush();
        }
        else if (str.compareTo("String") == 0) {
           out.writeUTF(sparse(str2)); out.flush();
        }
        else if (str.compareTo("Object") == 0) {
           out.writeObject(oparse(str2));
        }
      }
      catch (Exception e){e.printStackTrace();}

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
