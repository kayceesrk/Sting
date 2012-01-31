import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

//to run: sessionj -cp . -Dsessionj.transports.session=a Client // RAY: no need to specify transports anymore.
//$ bin/sessionj -cp tests/classes/ Server

public class Server {

  protocol recSide rec X[?{QUIT: , REC: ?(int).!<MyObject>.#X}]
  protocol rcv ?(int).!<MyObject>.@(recSide)
  protocol types {@(rcv), @(recSide)}
  protocol serverSide sbegin.@(recSide)
  
  private int port;
  private int numClients;
  private long count = 0;

  private static long t = 0;
  private static int signal = MyObject.NO_SIGNAL;

  private static boolean counting = false;

  public Server(int port, int numClients) {
    this.port = port;
    this.numClients = numClients;
  }

  public void run() {

  	SJSessionParameters params = null;
  	
  	try
  	{
	  	params = SJTransportUtils.createSJSessionParameters("s", "a");
  	}
  	catch(Exception e){e.printStackTrace();}	
	  	
	    final noalias SJSelector sel = SJRuntime.selectorFor(types);
	    noalias SJServerSocket ss;
	    noalias SJSocket s;

	    try(ss) {
	      //ss = SJServerSocket.create(serverSide, port);
	    	ss = SJServerSocket.create(serverSide, port, params);
	      try(sel) {
	        sel.registerAccept(ss);
	        try (s) {
	          while(numClients != 0) {
	            s = sel.select();
	            typecase(s) {
	              when(@(recSide)) {
                  s.recursion(X) {
	                  s.inbranch() {
	                    case REC:
	                      sel.registerInput(s);
	                    case QUIT:
	                      numClients--;
 //                       System.out.println(numClients);
	                  }
	                }
	              }
	              when(@(rcv)) {
	                s.receiveInt();
	                s.send(new MyObject(signal));
                  if (counting) {
	                  count++;
                    //System.out.println("count:" + count);
                  }
	                sel.registerInput(s);
	              }
	            }
	          }
	        }
	        catch(Exception e){e.printStackTrace();}
	      }
	      catch(Exception e){e.printStackTrace();}
	    }
	    catch(Exception e){e.printStackTrace();}
  //    System.out.println("Server finished");
    t = System.nanoTime() - t;
    System.out.println("Throughput Count: " + count + ". Time: " + t);
  }

  public static void sendKill() {
    signal |= MyObject.KILL_LOAD;
  }

  public static void sendTiming() {
    signal |= MyObject.BEGIN_TIMING;
  }

  public static void sendCounting() {
    counting = true;
  }

  public static void main(String [] args) {
    t = System.nanoTime();
    new Server(2000, 200).run();
  }

}
