import java.util.concurrent.*;

//sessionj -cp . -Dsessionj.transports.session=a ServerRunner // RAY: no need to specify transports anymore.
//$ bin/sessionj -cp tests/classes/ ServerRunner 2000 1

public class ServerRunner {

  public static void main(String args[]) {
    if(args.length < 3) {
      System.out.println("Usage: sessionj ServerRunner <port> <clientNum> <msg size>");
      return;
    }

    final int port = Integer.parseInt(args[0]);
    MyObject.DEFAULT_SIZE = Integer.parseInt(args[2]);

    final int cn = Integer.parseInt(args[1]);

    new Thread() {
      public void run() {
        new Server(port, cn).run();
      }
    }.start();

    new Thread() {
      public void run() {
        new SignalServer(port + 1).run();
      }
    }.start();

  }
}
