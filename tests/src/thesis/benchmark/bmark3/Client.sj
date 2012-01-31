//$ bin/sessionj -cp tests/classes/ thesis.benchmark.bmark3.Client false localhost 8888 5 1 true 

package thesis.benchmark.bmark3;

import java.util.Arrays;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJProtocol;
import sessionj.runtime.net.SJService;
import sessionj.runtime.net.SJSocket;

import thesis.benchmark.bmark3.Common;
import thesis.benchmark.bmark3.Particle;
import thesis.benchmark.bmark3.ParticleV;

public class Client
{	
	private boolean debug;
	private String host; 
	private int port; 
	private int numParticles;
	private int offset;
	private boolean timer;
	
	public Client(boolean debug, String host, int port, int numParticles, int offset, boolean timer)
	{
		this.debug = debug;
		this.host = host;
		this.port = port;
		this.numParticles = numParticles;
		this.offset = offset;
		this.timer = timer;
	}
	
	public void run() throws Exception
	{
		final noalias SJService c = SJService.create(Common.NBODY_CLIENT, host, port);
		final noalias SJSocket s;
		try (s)
		{ 				
			Particle[] particles = new Particle[numParticles];
			ParticleV[] pvs = new ParticleV[numParticles];
			initParticles(particles, pvs, offset);
			
			Common.debugPrintln(debug, "Initial: " + Arrays.toString(particles));
			
			s = c.request();			
			s.send(timer);
			s.send(particles);
			s.send(pvs);
			particles = (Particle[]) s.receive();
			
			Common.debugPrintln(debug, "Results: " + Arrays.toString(particles));
		}
		finally { }
	}

	private void initParticles(Particle[] particles, ParticleV[] pvs, int offset)
	{
		for(int i = 0; i < particles.length; i++)
		{		
			Particle p = new Particle();
			ParticleV pv = new ParticleV();
			
			if (debug)
			{
				p.x = i + offset;
				p.y = i + offset;
				p.m = 1.0 + offset;
			}
			else
			{
				p.x = 10.0 * Math.random();
				p.y = 10.0 * Math.random();
				p.m = 10.0 * Math.random();
			}
			
			pv.vi_old = 0;
			pv.vj_old = 0;
			pv.ai_old = 0;
			pv.aj_old = 0;
			pv.ai = 0;
			pv.aj = 0;
	
			particles[i] = p;			
			pvs[i] = pv;
		}
	}
	
	public static void main(String args[]) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String host = args[1];
		int port = Integer.parseInt(args[2]);
		int numParticles = Integer.parseInt(args[3]);
		int offset = Integer.parseInt(args[4]);
		boolean timer = Boolean.parseBoolean(args[5]);
		
		Client c = new Client(debug, host, port, numParticles, offset, timer); 		
		c.run();
	}	
}
