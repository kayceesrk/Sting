package aplas.bmarks2.micro;

import java.io.*;
import java.net.*;

import aplas.bmarks2.micro.*;

abstract public class Client {
	private boolean debug;

	private String host;
	private int port;

	private int cid;
	private int serverMessageSize;

	public Client(boolean debug, String host, int port, int cid, int serverMessageSize) {
		this.debug = debug;  	
		this.host = host;
		this.port = port;    
		this.cid = cid;
		this.serverMessageSize = serverMessageSize;  	
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	protected int getCid() {
		return cid;
	}

	protected int getServerMessageSize() {
		return serverMessageSize;
	}

	abstract public void run() throws Exception;

	public final boolean isDebug() {
		return debug;
	}

	public void debugPrintln(String m) {
		Common.debugPrintln(debug, m);
	}
}
