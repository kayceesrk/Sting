package aplas.bmarks2.micro;

import java.io.*;
import java.net.*;

import aplas.bmarks2.micro.*;

// Also used by macro benchmarks.
public class SignalServer {
	public static final int SIGNAL_SERVER_PORT_OFFSET = -100;

	public static final int KILL = 1;
	public static final int START_COUNTING = 2;
	public static final int STOP_COUNTING = 3;

	private boolean debug; 
	private int port;

	private Server server;

	private long start;
	private long finish;

	public SignalServer(boolean debug, int port, Server server) {
		this.debug = debug;
		this.port = port;
		this.server = server;
	}

	public void run() throws Exception {
		ServerSocket ss = null;

		Socket s  = null; 

		ObjectInputStream is = null;

		try {
			ss = new ServerSocket(port + SIGNAL_SERVER_PORT_OFFSET);

			Common.debugPrintln(debug, "[SignalServer] Listening on: " + (port + SIGNAL_SERVER_PORT_OFFSET));

			for (boolean run = true; run; ) {
				s = ss.accept();

				is = new ObjectInputStream(s.getInputStream());

				int signal = is.readInt();

				Common.debugPrintln(debug, "[SignalServer] Read: " + signal);

				switch (signal) {
					case KILL:
						run = false;
						server.kill();
						break;
					case START_COUNTING:
						server.startCounting();
						break;
					case STOP_COUNTING:
						server.stopCountingAndReset();
						break;
				}
			}
		}
		finally {
			Common.closeInputStream(is);
			Common.closeSocket(s);
			Common.closeServerSocket(ss);
		}

		Common.debugPrintln(debug, "[SignalServer] Finished.");
	}
}
