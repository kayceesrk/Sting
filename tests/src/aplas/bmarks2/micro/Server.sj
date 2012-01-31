package aplas.bmarks2.micro;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.ConcurrentHashMap;

import aplas.bmarks2.micro.*;

// Also used by macro benchmarks.
abstract public class Server {
	private boolean debug;		
	private int port;

	/*protected int numClients;
	protected Object lock = new Object();*/
	private AtomicInteger numClients = new AtomicInteger(0);

	volatile private long startTime; 
	volatile private long finishTime; 

	volatile private boolean count; 
	private int[] counts; // The number of messages sent.    
	// ConcurrentHashMap so that several threads can update different counters safely. 
	// No guarantees if 2 threads try to update the same counter, though.
	private Map fairnessCounters = new ConcurrentHashMap();

	public Server(boolean debug, int port) {
		this.debug = debug;
		this.port = port;
	}

	public final int getPort() {
		return port;
	}

	abstract public void run() throws Exception; // Allow Clients to connect and run sessions. 
	abstract public void kill() throws Exception; // Wait for Clients to finish and then end. 

	protected final int addClient() {
		return numClients.incrementAndGet();
	}

	public final int getNumClients() {
		return numClients.get();
	}

	public final int removeClient() {
		return numClients.decrementAndGet();
	}

	public final void startCounting() {
		this.counts = new int[getNumClients()]; // All clients should be connected before this is called.
		this.startTime = System.nanoTime();
		this.count = true;
	}

	public final boolean isCounting() {
		return this.count; 
	}

	public final int incrementCount(int index) {// Not synchronized because we expect only a single thread to access each element.
		this.counts[index]++;
		return this.counts[index];
	}

	public final void stopCountingAndReset() { 
		this.count = false;
		this.finishTime = System.nanoTime();

		System.out.println("[Server] Count duration: " + (this.finishTime - this.startTime) + " nanos");
		System.out.println("[Server] Total count: " + getCountTotal());
	}

	public final long getCountTotal() {
		long total = 0;

		for (int i = 0; i < this.counts.length; i++) {
			total += this.counts[i];
		}

		return total;  	
	}

	public final boolean isDebug() {
		return debug;
	}

	public final void debugPrintln(String m) {
		Common.debugPrintln(debug, m);
	}

	public final void incrementFairnessCounter(int localPort) {
		Integer port = Integer.valueOf(localPort);
		Integer count = (Integer) fairnessCounters.get(port);
		if (count == null) {
			count = Integer.valueOf(1);
		}
		fairnessCounters.put(port, Integer.valueOf(count.intValue() + 1));
	}

	public final void printFairnessCounters() {
		System.out.println("[Server] Counts per local port: " + fairnessCounters);  
	}
}
