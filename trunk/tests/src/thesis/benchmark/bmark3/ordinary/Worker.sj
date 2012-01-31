//$ bin/sessionjc -cp tests/classes/ tests/src/thesis/benchmark/bmark3/ordinary/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark3.ordinary.Worker false 4442 localhost 4440 

package thesis.benchmark.bmark3.ordinary;

import java.util.Arrays;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.net.SJServerSocket;
import sessionj.runtime.net.SJServerSocketCloser;
import sessionj.runtime.net.SJService;
import sessionj.runtime.net.SJSocket;

import thesis.benchmark.Killable;
import thesis.benchmark.bmark3.Common;
import thesis.benchmark.bmark3.Particle;
import thesis.benchmark.bmark3.ParticleV;

public class Worker implements Killable
{
	private volatile boolean run = true;
	private volatile boolean finished = false;
	private SJServerSocketCloser ssc;
	
	private boolean debug;
	private int port;  
	private int port_l;
	private String host_r;
	private int port_r; 
	
	public Worker(boolean debug, int port, int port_l, String host_r, int port_r)
	{
		this.debug = debug;
		this.port = port;  
		this.port_l = port_l;
		this.host_r = host_r;
		this.port_r = port_r; 
	}	
	
	public void run() throws Exception
	{			
		final noalias SJServerSocket ss;
		final noalias SJServerSocket ss_l;
		try (ss, ss_l)
		{				
			ss = SJServerSocket.create(Common.NBODY_SERVER, port);
			ssc = ss.getCloser();
			
			Common.debugPrintln(debug, "[Worker] Service started on port: " + port);
			
			ss_l = SJServerSocket.create(Common.LINK_SERVER, port_l);			
			
			final noalias SJService c_r = SJService.create(Common.LINK_CLIENT, host_r, port_r);			
			while (run)
			{
				final noalias SJSocket s;				
				final noalias SJSocket s_r;
				final noalias SJSocket s_l;										
				try (s, s_r, s_l) 
				{
					s = ss.accept();
					
					Common.debugPrintln(debug, "[FirstWorker] Accepted client.");
					
					s.receiveBoolean(); // Discarded (only used by LastWorker)
					Particle[] particles = (Particle[]) s.receive();
					ParticleV[] pvs = (ParticleV[]) s.receive();		
					
					Common.debugPrintln(debug, "[FirstWorker] Initial: " + Arrays.toString(particles));
					
					s_r = c_r.request();
					s_l = ss_l.accept();						
					
					Common.debugPrintln(debug, "[FirstWorker] Created left link and accepted right link.");
					
					s_l.send(s_r.receiveInt() + 1);				
					int i = 0;																		
					s_r.outwhile(s_l.inwhile())
					{								
						Particle[] current = new Particle[particles.length];										
						System.arraycopy(particles, 0, current, 0, current.length);					
						s_r.outwhile(s_l.inwhile())
						{			
							s_r.send(current);
							Common.computeForces(particles, current, pvs);						
							current = (Particle[]) s_l.receive();
						}									
						Common.computeForces(particles, current, pvs);					
						Common.computeNewPos(particles, pvs, i);												
						i++;
						
						Common.debugPrintln(debug, "\n[Worker] Step: " + i);
						Common.debugPrintln(debug, "[Worker] Particles: " + Arrays.toString(particles));
					}			
					s.send(particles);
				}
				finally { }
				
		  	System.gc();
		  	Thread.sleep(Common.ITERATION_DELAY);
			}
		}		
		finally 
		{ 			
			finished = true;
		}
	}
	
  public void kill() throws Exception
  {  	  	
  	run = false; // It's important that no more clients are trying to connect after this point		
  	ssc.close(); // Break the accepting loop (make the blocked accept throw an exception)		
		while (!this.finished);
  }	
	
	public static void main(String args[]) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		int port_l = Integer.parseInt(args[2]);
		String host_r = args[3];
		int port_r = Integer.parseInt(args[4]);				

		Worker w = new Worker(debug, port, port_l, host_r, port_r); 
		w.run();
	}	
}
