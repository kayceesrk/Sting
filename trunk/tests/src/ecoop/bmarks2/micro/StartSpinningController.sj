package ecoop.bmarks2.micro;

import java.net.*;

public class StartSpinningController implements Runnable {
	public static final int OFFSET = -200;
	private final int port;
	private final int numWorkers;
	private final Socket[] sockets;
	private final boolean debug;
	
	public StartSpinningController(boolean debug, int port, int numWorkers) {
		this.port = port;
		this.numWorkers = numWorkers;
		sockets = new Socket[numWorkers];
		this.debug = debug;
	}
	
	public void run() {
		ServerSocket ss = null;
		Socket s = null;
		try {
			ss = new ServerSocket(port + OFFSET);
			Common.debugPrintln(debug, "[StartSpinningController] Listening on: "+ (port+OFFSET) + ", waiting for " + numWorkers + " clients");
			for (int i=0; i<numWorkers; i++) {
				s = ss.accept();
				sockets[i] = s;
			}
			// All workers have connected, they can now start spinning.
			for (int i=0; i<numWorkers; i++) {
				sockets[i].sendUrgentData(42);
			}
			System.out.println("[StartSpinningController] Sent spin signal to all clients");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			Common.closeServerSocket(ss);
			for (int i=0; i<numWorkers; i++) {
				Common.closeSocket(sockets[i]);
			}
		}
	}
}
