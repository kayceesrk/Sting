//$ bin/sessionj -cp tests/classes/ ecoop.bmarks2.micro.ClientRunner false localhost 8888 -1 100 1 10 JT
//$ bin/sessionj -Dsessionj.transports.session=a -cp tests/classes/ ecoop.bmarks2.micro.ClientRunner false localhost 8888 -1 100 1 10 SE

package aplas.bmarks2.micro;

import java.io.*;
import java.net.*;

// Spawns LoadClients.
public class ClientRunner {
	public static void main(String [] args) throws Exception {
		
		final boolean debug = Boolean.parseBoolean(args[0].toLowerCase());
		final String host = args[1];
		final int serverPort = Integer.parseInt(args[2]);
		final int scriptPort = Integer.parseInt(args[3]);

		int delay = Integer.parseInt(args[4]);
		final int numClients = Integer.parseInt(args[5]);    
		final int serverMessageSize = Integer.parseInt(args[6]);

		final String flag = args[7];

		if (!(/*flag.equals(ServerRunner.JAVA_THREAD) || flag.equals(ServerRunner.JAVA_EVENT) ||*/ flag.equals(ServerRunner.SJ_THREAD) || flag.equals(ServerRunner.SJ_EVENT))) {
			System.out.println("[ClientRunner] Bad server flag: " + flag);

			return;
		}   

		final boolean[] ack = new boolean[] { false };
		final boolean[] spin = new boolean[] { false };  	

		for (int i = 0; i < numClients; i++) {
			final int cid = i;

			new Thread() {
				public void run() {
					try {        		
						/*if (flag.equals(ServerRunner.JAVA_THREAD)) {
							new ecoop.bmarks2.micro.java.thread.client.LoadClient(debug, host, serverPort, cid, serverMessageSize, ack, spin).run();
						}
						else if (flag.equals(ServerRunner.JAVA_EVENT)) {
							new ecoop.bmarks2.micro.java.event.client.LoadClient(debug, host, serverPort, cid, serverMessageSize, ack, spin).run();
						}
						else*/ if (flag.equals(ServerRunner.SJ_THREAD) || flag.equals(ServerRunner.SJ_EVENT)) {
							new aplas.bmarks2.micro.te.client.LoadClient(debug, host, serverPort, cid, serverMessageSize, ack, spin).run();
						}
						else {
							System.out.println("[ClientRunner] Unrecognised flag: " + flag);
							System.exit(0);
						}
					}
					catch (Exception x) {
						throw new RuntimeException(x);
					}
				}
			}.start();

			synchronized (ack) {
				while (!ack[0]) {
					ack.wait();
				}
			}

			ack[0] = false;

			System.out.println("[ClientRunner] Ack received from Client: " + cid);

			try {
				Thread.sleep(delay);
			}
			finally{
			}
		}

		// Here, threads have been created (and started?) but the LoadClients are not necessarily connected yet. 
		if (scriptPort > 0) {
			Socket s = null;
			//DataOutputStream dos = null;

			try {
				String script;

				if (host.equals("localhost")) { // FIXME: stupid hack for python server sockets.
					script = host;
				}
				else {
					script = InetAddress.getLocalHost().getHostName();
				}

				s = new Socket(script, scriptPort);

				//dos = new DataOutputStream(s.getOutputStream());
				//dos.writeInt(3);
				//dos.flush();
			}
			finally {
				//Common.closeOutputStream(dos);				
				Common.closeSocket(s);
			}
		}

		Socket s1 = null;
		InputStream is = null;
		try {
			Common.debugPrintln(debug, "Waiting for spinning signal from server...");
			s1 = new Socket(host, serverPort+StartSpinningController.OFFSET);
			is = s1.getInputStream();
			is.read();
		}
		finally {
			Common.closeSocket(s1);
			Common.closeInputStream(is);
		}
		
		synchronized (spin) {
			spin[0] = true;
			spin.notifyAll();
		}
		Common.debugPrintln(debug, "Received start signal from server, spinning");
	}
}
