
import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

//sessionj -cp tests/classes/ Client

public class Client {

  protocol clientSide cbegin.rec X[!{QUIT: , REC: !<int>.?(MyObject).#X}]

  public static int port = 2000;
  public static String host = "";

  private boolean timing;
  private int iterations;

  /*Use to simulate begin timing and kill load signals*/
  public boolean beginTiming;
  public boolean killLoad;

  private int clientNum;
  private long []times;

  /*Load Client*/
  public Client(int clientNum) {
    this.timing = false;
    this.beginTiming = false;
    this.killLoad = false;
    this.iterations = 0;  
    this.clientNum = clientNum;
  }
  
  /*Time Client*/
  public Client(int clientNum, int iterations) {
    this.timing = true;
    this.beginTiming = false;
    this.killLoad = false;
    this.iterations = iterations;
    this.times = new long[iterations];
    this.clientNum = clientNum;
  }

  public void client() 
  {
  	SJSessionParameters params = null;
  	
  	try
  	{
  		params = SJTransportUtils.createSJSessionParameters("s", "a");
  	}
  	catch (Exception x)
  	{
  		x.printStackTrace();
  	}
  	
  	final noalias SJService serv = SJService.create(clientSide, host, port);
    noalias SJSocket s;
    int i = 0;
    MyObject mo = null;

    try(s) {
      s = serv.request(params);

      s.recursion(X) {
        if(!this.killLoad) {
          if (this.beginTiming && this.timing && (i < iterations)) {
              times[i] = System.nanoTime();
          }
          s.outbranch(REC) {
            s.send(i);
            mo =(MyObject) s.receive();
            killLoad = mo.killSignal();
            beginTiming = mo.timeSignal();
         //   System.out.println(clientNum + ":" + killLoad + ":" + beginTiming);

            if (this.beginTiming && this.timing && (i < iterations)) {
              times[i] = System.nanoTime() - times[i];
              i++;
            }
            s.recurse(X);
          }
        }
        else {
          s.outbranch(QUIT) {
 //           System.out.println(clientNum + " sends quit");
          }
        }
      }

    }
    catch (Exception e) {e.printStackTrace();}

    if (timing) {
      for (int j = 0; j < iterations; j++)
        System.out.println("Client Number: " + clientNum + ".Iteration: " + j + ". Time: " + times[j] + ".");
    }
    
  }

  public static void main(String [] args) {

    if (args.length < 5) {
      System.out.println("Usage: sessionj ClientRunner <host> <port> <Timed Clients> <session length> <msg size>");
      return;
    }

    Client.host = args[0];
    Client.port = Integer.parseInt(args[1]);

    //int loadClients = Integer.parseInt(args[2]);
    int timedClients = Integer.parseInt(args[2]);
    final int iterations = Integer.parseInt(args[3]);
    MyObject.DEFAULT_SIZE = Integer.parseInt(args[4]);
    
    int i;

    for (i = 0; i < timedClients; i++)	{
      final int j = i;
      new Thread() {
        public void run() {
          new Client(j, iterations).client();
         // System.out.println(j + " finished");
        }
      }.start();
    }

  }

}
