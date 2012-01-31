package sessionj.benchmark.SJE;

public class ServerRunner {

  public static void main(String []args) {
    if (args.length < 3) {
      System.out.println("Usage: sessionj ServerRunner <server name> <port> <client num>");
      return;
    }

    String server = args[0];
    int port = Integer.parseInt(args[1]);
    int clientNum = Integer.parseInt(args[2]);
    Server s = null;

    if (server.equals("Simple")) {
      s = new SimpleServer();
    }
    else if (server.equals("Request")) {
      s = new RequestServer();
    }
    else if (server.equals("Type")) {
      s = new TypeServer();
    }

    long start = System.nanoTime();
    s.server(port, clientNum);
    long end = System.nanoTime();

    System.out.println(end - start);
  }

}
