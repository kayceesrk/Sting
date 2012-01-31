package ecoop.bmarks.threadsj;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import ecoop.bmarks.*;

//to run: sessionj -cp . -Dsessionj.transports.session=a Client // RAY: no need to specify transports anymore.
//$ bin/sessionj -cp tests/classes/ Server

//@deprecated
public class Server {

		protocol recSide rec X[?{QUIT: , REC: ?(int).!<MyObject>.#X}]
 // protocol rcv ?(int).!<MyObject>.@(recSide)
 // protocol types {@(rcv), @(recSide)}
 		protocol serverSide sbegin.@(recSide)
  
  	private int port;
 		private int numClients;
  	private long count = 0;
	
		private static boolean debug;

  	private static long t = 0;
  	public static int signal = MyObject.NO_SIGNAL;

  	private static boolean counting = false;

  	public Server(boolean debug, int port, int numClients) {

				Server.debug = debug;

    		this.port = port;
    		this.numClients = numClients;
  	}

  	static class ServerThread extends SJThread {

			public void srun(noalias @(sendInt) s) {

		  	try (s) {
					
      			s.recursion(X) {
	        			s.inbranch() {
	          	  		case REC:
													ClientMessage m = (ClientMessage) s.readObject();
													s.send(new MyObject(signal, m.getSize()));
													if (counting) 
	          							{
	          	  						count++;
								            debugPrintln("[Server] Current count:" + count);
	          							}
													s.recurse(X);
	          	      case QUIT:
	          	      		numClients--;
	          	          debugPrintln("[Server] Clients remaning: " + numClients);
	          	  }
		        } 	   
		    }
		    catch(Exception e){e.printStackTrace();}
			}

		}


	  public void server(int port, int numClient) {
		    noalias SJServerSocket ss;
		    noalias SJSocket s;
		    SJSessionParameters params = null;

		  	try
		  	{
				  	params = SJTransportUtils.createSJSessionParameters("s", "a");
		  	}
		  	catch(Exception e){e.printStackTrace();}


		    try (ss) {
			      ss = SJServerSocket.create(serverSide, port, params);
			      while (numClient -- != 0) {
		        		try (s) {
										s = ss.accept();
			          		<s>.spawn(new ServerThread());
      		  		} catch(Exception e) {e.printStackTrace();}
      		    		finally {}
      			}
    		} catch(Exception e) {e.printStackTrace();}
      		finally {}
  	}


		private static final void debugPrintln(String m)
  	{
  			if (debug)
  			{
  				System.out.println(m);
  			}
  	}

		public static void main(String [] args) throws Exception 
  	{
  			boolean debug = Boolean.parseBoolean(args[0]);
  			int port = Integer.parseInt(args[1]);
  			int numClients = Integer.parseInt(args[2]);
  	
		    new Server(debug, port, numClients).run();
  	}

}

