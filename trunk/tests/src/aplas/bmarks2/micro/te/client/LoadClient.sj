//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.sj.client.LoadClient false localhost 8888 -2 100
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks2.micro.sj.client.LoadClient false localhost 8888 -2 100

package aplas.bmarks2.micro.te.client;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

import aplas.bmarks2.micro.*;

// For both "thread" and "event" SJ Servers.
public class LoadClient extends aplas.bmarks2.micro.LoadClient {

	protocol pClient cbegin.rec X [!{REC: !<ClientMessage>.?(ServerMessage).!<ClientMessage>.!{L1: ?(ServerMessage).#X, L2: ?(ServerMessage).#X}, QUIT: }]
	//protocol pClient cbegin.rec X [!{REC: !<ClientMessage>.?(ServerMessage).#X, QUIT: }]

	public LoadClient(boolean debug, String host, int port, int cid, int serverMessageSize, boolean[] ack, boolean[] spin) {
		super(debug, host, port, cid, serverMessageSize, ack, spin);
	}

	public void run() throws Exception {

		final noalias SJService c = SJService.create(pClient, getHost(), getPort());

		final noalias SJSocket s;

		try (s) {
			s = c.request();

			sendAck();

			boolean debug = isDebug();
			int cid = getCid();
			int serverMessageSize = getServerMessageSize();

			ServerMessage sm;

			boolean run = true; 
			int iters = 0;

			s.recursion(X) {
				if (run) {
					s.outbranch(REC) {
						s.send(new ClientMessage(cid, Integer.toString(iters), cid, serverMessageSize));

						sm = (ServerMessage) s.receive();

//						run = !sm.isKill();
						s.send(new ClientMessage(cid, Integer.toString(iters), iters++, serverMessageSize));

						if (cid % 2 == 0) {
							s.outbranch(L1) {
								sm = (ServerMessage) s.receive();
								run = !sm.isKill();

								debugPrintln("[LoadClient " + cid + "] Received: " + sm);

								if (debug) {
									Thread.sleep(1000);
								}
								waitSpin();
								s.recurse(X);
							}
						}
						else {
							s.outbranch(L2) {
								sm = (ServerMessage) s.receive();
								run = !sm.isKill();

								debugPrintln("[LoadClient " + cid + "] Received: " + sm);

								if (debug) {
									Thread.sleep(1000);
								}
								waitSpin();
								s.recurse(X);
							}
						}	

					}
				}
				else {
					s.outbranch(QUIT) {
						debugPrintln("[LoadClient " + cid + "] Quitting.");
					}
				}
			}

			Thread.sleep(50);
		}
		finally{
		}
	}

	public static void main(String [] args) throws Exception  {
		boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		int cid = Integer.parseInt(args[3]);
		int serverMessageSize = Integer.parseInt(args[4]);

		new LoadClient(debug, host, port, cid, serverMessageSize, new boolean[1], new boolean[1]).run();  
	}
}
