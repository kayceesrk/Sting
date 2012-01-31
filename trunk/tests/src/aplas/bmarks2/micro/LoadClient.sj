package aplas.bmarks2.micro;

import java.io.*;
import java.net.*;

import aplas.bmarks2.micro.*;

abstract public class LoadClient extends Client {
	private final boolean[] ack;
	private final boolean[] spin;

	/*public LoadClient(boolean debug, String host, int port, int cid, int serverMessageSize) 
	{
	super(debug, host, port, cid, serverMessageSize);
	}*/

	public LoadClient(boolean debug, String host, int port, int cid, int serverMessageSize, boolean[] ack, boolean[] spin) {
		super(debug, host, port, cid, serverMessageSize);

		this.ack = ack;
		this.spin = spin;
	}  

	public final void sendAck() {
		synchronized (this.ack) {
			this.ack[0] = true;

			this.ack.notify();
		}
	}  

	public final void waitSpin() throws InterruptedException {
		synchronized (spin) {
			while (!spin[0])
				spin.wait();
		}
	}
}
