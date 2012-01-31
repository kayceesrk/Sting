

public class ServerRunner {

  public static void main(String []args) {
    if (args.length != 4) {
      System.out.println("Usage: java ServerRunner <server name> <port> <thread num> <client num>");
      return;
    }

    String server = args[0];
    int port = Integer.valueOf(args[1]);
    int threadNum = Integer.valueOf(args[2]);
    int clientNum = Integer.valueOf(args[3]);
    Server s = null;

    if (server.equals("Simple")) {
      s = new SimpleServer(threadNum);
    }
    else if (server.equals("Request")) {
      s = new RequestServer(threadNum);
    }
    else if (server.equals("Type")) {
      s = new TypeServer(threadNum);
    }

    long start = System.nanoTime();
    s.server(port, clientNum);
    long end = System.nanoTime();

    System.out.println(end - start);
  }

}
