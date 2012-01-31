//$ bin/sessionjc -cp tests/classes/ tests/src/thesis/benchmark/bmark3/ordinary/LastWorker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark3.ordinary.LastWorker false 8888 localhost 4441 localhost 4442 2 1 BODY

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
import thesis.benchmark.bmark3.NbodyTimer;
import thesis.benchmark.bmark3.Particle;
import thesis.benchmark.bmark3.ParticleV;

public class LastWorker extends NbodyTimer implements Killable
{	
	private static protocol LAST_LINK_CLIENT cbegin.![![?(Particle[])]*]* // No ring token message
	
	private volatile boolean run = true;
	private volatile boolean finished = false;
	private SJServerSocketCloser ssc;	
	 
	private int port; 
	private String host_l; 
	private int port_l; 
	private String host_r; 
	private int port_r; 
	//private int numParticles;
	private int numProcessors = 0;
	private int steps;
	
	public LastWorker(boolean debug, int port, String host_l, int port_l, String host_r, int port_r, /*int numParticles, int numProcessors,*/ int steps, int iters, String flag)
	{
		super(debug, iters, flag);
		this.port= port; 
		this.host_l = host_l; 
		this.port_l= port_l; 
		this.host_r = host_r; 
		this.port_r = port_r; 
		//this.numParticles = numParticles;
		this.steps = steps;
	}
	
	//public void run(boolean warmup, boolean timer) throws Exception
	public void run() throws Exception
	{
		//boolean debug = isDebug();		
		//int len = (warmup && !debug) ? NbodyTimer.WARMUP_SESSION_LENGTH : steps;
		int len = steps;
		
		final noalias SJServerSocket ss;				
		try (ss)
		{						
			ss = SJServerSocket.create(Common.NBODY_SERVER, port);
			ssc = ss.getCloser();
			
			debugPrintln("[LastWorker] Service started on port: " + port);		

			final noalias SJService c_r = SJService.create(Common.LINK_CLIENT, host_r, port_r);
			final noalias SJService c_l = SJService.create(LAST_LINK_CLIENT, host_l, port_l);

			while (run)
			{
				final noalias SJSocket s;
				final noalias SJSocket s_r;
				final noalias SJSocket s_l;
				boolean timer = false;
				try (s, s_r, s_l)
				{ 				
					s = ss.accept();					
	
					debugPrintln("[LastWorker] Accepted client.");
					
					timer = s.receiveBoolean();
					Particle[] particles = (Particle[]) s.receive();
					ParticleV[] pvs = (ParticleV[]) s.receive();					
					
					debugPrintln("[LastWorker] Initial: " + Arrays.toString(particles));
					
					s_r = c_r.request();
					s_l = c_l.request();
	
					debugPrintln("[LastWorker] Created left and right links.");
					
					startTimer();
					
					numProcessors = s_r.receiveInt() + 1;
					
					debugPrintln("[LastWorker] Number of processors: " + numProcessors);
													
					int i = 0;				
					<s_r, s_l>.outwhile(i < len)
					{	
						Particle[] current = new Particle[particles.length];					
						System.arraycopy(particles, 0, current, 0, current.length);				
						int j = 0;					
						<s_r, s_l>.outwhile(j < (numProcessors - 1))
						{									
							s_r.send(current);	
							Common.computeForces(particles, current, pvs);													
							current = (Particle[]) s_l.receive();													
							j++;
						}																						
						Common.computeForces(particles, current, pvs);										
						Common.computeNewPos(particles, pvs, i);											
						i++;
						
						debugPrintln("\n[LastWorker] Step: " + i);
						debugPrintln("[LastWorker] Particles: " + Arrays.toString(particles));						
					}
	
			  	stopTimer();
					
					s.send(particles);
				}
				finally { }				
				
			  if (timer)
		  	{
		  		printTimer();
		  	}	 	  			  
		  	resetTimer();
		  	
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
		String host_l = args[2];
		int port_l = Integer.parseInt(args[3]);
		String host_r = args[4];
		int port_r = Integer.parseInt(args[5]);				
		//int numParticles = Integer.parseInt(args[6]);
		//int numProcessors = Integer.parseInt(args[7]); // Determined from the ring token		
		int steps = Integer.parseInt(args[6]);
		int iters = Integer.parseInt(args[7]);
		String flag = args[8];
		
		/*if (numParticles > Common.MAX_PARTICLES/* && numParticles <= MAX_PROCESSORS*)
		{	
			throw new RuntimeException("[LastWorker] Too many particles: " + numParticles);
		}*/
		
		LastWorker lw = new LastWorker(debug, port, host_l, port_l, host_r, port_r, steps, iters, flag); 		
		lw.run();
	}	
}
