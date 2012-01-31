//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks2.micro.sj.event.server.Server false 8888

package aplas.bmarks2.micro.te.event.server;

import java.util.*;
import java.util.concurrent.atomic.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import aplas.bmarks2.micro.*;

public class Server extends aplas.bmarks2.micro.Server {
	public protocol pRecursion rec X [?{REC: ?(ClientMessage).!<ServerMessage>.?(ClientMessage).?{L1: !<ServerMessage>. #X,L2: !<ServerMessage>.#X}, QUIT: }]
	public protocol pReceive1 ?(ClientMessage).!<ServerMessage>.?(ClientMessage).?{L1: !<ServerMessage>.@(pRecursion),L2: !<ServerMessage>.@(pRecursion)}
	public protocol pReceive2 ?(ClientMessage).?{L1: !<ServerMessage>. @(pRecursion),L2: !<ServerMessage>.@(pRecursion)}
	public protocol pReceive3 ?{L1: !<ServerMessage>. @(pRecursion),L2: !<ServerMessage>.@(pRecursion)}


/*	public protocol pRecursion rec X [?{REC: ?(ClientMessage).!<ServerMessage>.#X, QUIT: }]
	public protocol pReceive ?(ClientMessage).!<ServerMessage>.@(pRecursion) 
*/
	public protocol pServer sbegin.@(pRecursion)

	private protocol pSelector { @(pRecursion), @(pReceive1), @(pReceive2), @(pReceive3) }

	volatile private boolean run = true;
	volatile private boolean kill = false;
	volatile private boolean finished = false;

	private Set clients = new HashSet();

	public Server(boolean debug, int port) {
		super(debug, port);
	}


	public void run() throws Exception {

		final noalias SJSelector sel = SJRuntime.selectorFor(pSelector);

		ClientMessage cm = null;
		HashMap cm1 = new HashMap(128, (float) 0.5);
		HashMap cm2 = new HashMap(128, (float) 0.5);

		try (sel) {
			noalias SJServerSocket ss;

			try (ss) {
				ss = SJServerSocket.create(pServer, getPort());

				debugPrintln("[Server] Listening on: " + getPort());

				sel.registerAccept(ss);
			}
			finally {
			}

			noalias SJSocket s;

			while (this.run) {
				try (s) { 
					s = sel.select();

					typecase (s) {
						when(@(pRecursion)) {
							Integer key = new Integer(s.getLocalPort());		        	

							if (!clients.contains(key)) {		        		
								addClient();
								clients.add(key);
							}

							s.recursion(X) {
								s.inbranch() {
									case REC: {
										sel.registerInput(s);
									}
									case QUIT: {
										removeClient();
										int numClients = getNumClients();
										debugPrintln("[Server] Clients remaning: " + numClients);

										if (numClients == 0) {// HACK: because the selector closer isn't working.
											this.run = false;
					                    }
									}
								}
							}
						}
						when(@(pReceive1)) {
							cm = (ClientMessage) s.receive();
							cm1.put(new Integer(s.getLocalPort()), cm);

							debugPrintln("[Server] Received: " + cm);
							boolean localKill = this.kill; // HACK.
							
							s.send(new ServerMessage(cm.getServerMessageSize(),cm.getNum() + 2, localKill));
							sel.registerInput(s);
						}
						when(@(pReceive2)) {
							cm = (ClientMessage) s.receive();
							cm2.put(new Integer(s.getLocalPort()), cm);

							debugPrintln("[Server] Received: " + cm);
							sel.registerInput(s);
						}
						when(@(pReceive3)) {
							s.inbranch() {
								case L1: {
									boolean localKill = this.kill; // HACK.
									cm = (ClientMessage) cm1.get(new Integer(s.getLocalPort()));
									s.send(new ServerMessage(cm.getServerMessageSize(), cm.getNum() + ((ClientMessage) cm2.get(new Integer(s.getLocalPort()))).getNum(), localKill));

									if (isCounting()) {
										incrementCount(0); // HACK: using a single counter (safe to do so for this single-threaded Server). Could store the "tids" in a map (using local ports as a key), but could be a non-neglible overhead.

										debugPrintln("[ServerThread] Current total count:" + getCountTotal());		            
									}
									sel.registerInput(s);
								}
								case L2: {
									boolean localKill = this.kill; // HACK.
									cm = (ClientMessage) cm1.get(new Integer(s.getLocalPort()));
									s.send(new ServerMessage(cm.getServerMessageSize(), cm.getNum() * ((ClientMessage) cm2.get(new Integer(s.getLocalPort()))).getNum(), localKill));

									if (isCounting()) {
										incrementCount(0); // HACK: using a single counter (safe to do so for this single-threaded Server). Could store the "tids" in a map (using local ports as a key), but could be a non-neglible overhead.

										debugPrintln("[ServerThread] Current total count:" + getCountTotal());		            
									}
									sel.registerInput(s);
								}
							}						
						}
					}
				}			  
				finally {
				}	
			}		  	  	    	    
		}
		catch (Exception x) {// Selector closer not currently working.
			x.printStackTrace();
		}
		finally {
			this.finished = true; // Comes before the inserted selector close operation.
		}
	}

	public void kill() throws Exception {
		int numClients = getNumClients(); 

		Thread.sleep(500);

		this.kill = true;
		
		while (!this.finished);
		/*if (!this.finished) {
			System.out.println("[Server] Forced exit... (" + numClients + ")");  		
			System.exit(0);
		}*/

		System.out.println("[Server] Finished running (" + numClients + " Clients joined).");
	}

	public static void main(String [] args) throws Exception {
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);

		new Server(debug, port).run();
	}
}
