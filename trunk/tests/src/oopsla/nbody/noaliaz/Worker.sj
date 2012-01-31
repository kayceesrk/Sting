//$ bin/sessionjc -cp tests/classes/ tests/src/nbody/noaliaz/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ nbody.noaliaz.Worker false 4443 localhost 4444 1

package nbody.noaliaz;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import nbody.Particle;
import nbody.ParticleV;

public class Worker
{
	private static final int MAX_PARTICLES = 4000;

	//private static final double G = 6.674 * Math.pow(10, -11);
	private static final double G = 1.0;
	
	private final noalias protocol ps_nbody // Server socket is on our s_l side: s_l guy connects to us.
	{
		sbegin.!<int>.?[?[?(Particle[])]*]*
	}
	
	private final noalias protocol pc_nbody // Client socket is on out s_r side: we connect to the s_r guy.
	{
		^(ps_nbody)
	}

	public void run(boolean debug, int port_l, String host_r, int port_r, int numParticles)
	{	
		// The particles.
		final noalias Particle[] particles = new Particle[numParticles];
		
		// The particles' velocities.
		ParticleV[] pvs = new ParticleV[numParticles]; 
		
		final noalias SJServerSocket ss_l;	
		
		final noalias SJService c_r = SJService.create(pc_nbody, host_r, port_r);		
		final noalias SJSocket s_r;
			
		/* Binary session types define a Client-Server model of programming; e.g. iterations and conditionals. */
		
		try (ss_l)
		{				
			ss_l = SJServerSocketImpl.create(ps_nbody, port_l);
			
			final noalias SJSocket s_l;							
			
			try (s_l, s_r) 
			{
				s_l = ss_l.accept();									
				
				s_r = c_r.request(); 
				
				s_l.send(s_r.receiveInt() + 1);
				
				initParticles(particles, pvs); 
											
				int i = 0;							
											
				s_r.outwhile(s_l.inwhile())
				{			
					if (debug)
					{
						System.out.println("\nIteration: " + i);
						System.out.println("Particles: " + noaliasArrayToString(particles));
					}				
				
					noalias Particle[] current = new Particle[numParticles];										
					
					noaliasArrayCopy(particles, current);
					
					s_r.outwhile(s_l.inwhile())
					{			
						computeForces(particles, current, pvs);					
					
						s_r.send(current);
						
						current = (Particle[]) s_l.receive();
					}
									
					computeForces(particles, current, pvs);
					
					computeNewPos(particles, pvs, i);						
					
					i++;
				}			

				if (debug)
				{
					System.out.println("\nIteration: " + i);
					System.out.println("Particles: " + noaliasArrayToString(particles));
				}				
			}
			finally
			{
								
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Worker] Non-dual behavior: " + ise);
		}
		catch (SJIOException sioe)
		{
			System.err.println("[Worker] Communication error: " + sioe);				
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[Worker] Class error: " + cnfe);
		}
		finally
		{
		
		}
	}
	
	public static void main(String args[])
	{
		boolean debug = Boolean.parseBoolean(args[0]);		
		int port_l = Integer.parseInt(args[1]);
		String host_r = args[2];
		int port_r = Integer.parseInt(args[3]);		
		
		int numParticles = Integer.parseInt(args[4]);
		
		if (numParticles <= MAX_PARTICLES/* && numParticles <= MAX_PROCESSORS*/)
		{	
			Worker w = new Worker(); 
			
			w.run(debug, port_l, host_r, port_r, numParticles);
		}
		else
		{
			System.out.println("Too many particles: " + numParticles);
		}
	}
	
	private void initParticles(final noalias Particle[] particles, ParticleV[] pvs)
	{
		for(int i = 0; i < particles.length; i++)
		{		
			noalias Particle p = new Particle();
			
			p.x = 10.0 * Math.random();
			p.y = 10.0 * Math.random();
			p.m = 10.0 * Math.random();
			
			/*p.x = i + 3;
			p.y = i + 3;						
			p.m = i + 3;*/
			
			particles[i] = p;
			
			ParticleV pv = new ParticleV();
			
			pv.vi_old = 0;
			pv.vj_old = 0;			
			pv.ai_old = 0;
			pv.aj_old = 0;
			pv.ai = 0;
			pv.aj = 0;
			
			pvs[i] = pv;
		}
	}
	
	protected static void computeForces(final noalias Particle[] particles, final noalias Particle[] current, ParticleV[] pvs)
	{		
		for (int ours = 0; ours < particles.length; ours++)
		{			
			double x = particles[ours].x;
			double y = particles[ours].y;
			
			double ai = 0.0;
			double aj = 0.0;
			
			for (int theirs = 0; theirs < current.length; theirs++)
			{				
				double ri = current[theirs].x - x;
				double rj = current[theirs].y - y;			
				double m = current[theirs].m;
				
				if (ri != 0)
				{									
					ai += ((ri < 0) ? -1 : 1) * G * m / (ri * ri);
				}
				
				if (rj != 0)
				{
					aj += ((rj < 0) ? -1 : 1) * G * m / (rj * rj);					
				}
			}
						
			pvs[ours].ai += ai;									
			pvs[ours].aj += aj;
		}	
	}
	
	protected static void computeNewPos(final noalias Particle[] particles, ParticleV[] pvs, int step)
	{
		double dt = 1.0;
			
		for(int i = 0; i < particles.length; i++)
		{
			ParticleV pv = pvs[i];
			
			double x = particles[i].x;
			double y = particles[i].y;			

			double ai_old = pv.ai_old;
			double aj_old = pv.aj_old;						
			double ai = pv.ai;
			double aj = pv.aj;

			// At this point we have p_n, a_n. We also have v_n-1 and a_n-1 (except for n = 0). So need to work out v_n (except for n = 0).
			
			double vi_old = pv.vi_old;
			double vj_old = pv.vj_old;			
			double vi;
			double vj;
			
			if (step == 0) // Only in the first iteration do we use the stored velocity values...
			{
				vi = vi_old; 
				vj = vj_old;
			}
			else // ...otherwise we calculate the current velocities.
			{
				vi = vi_old + ((ai + ai_old) * dt / 2);
				vj = vj_old + ((aj + aj_old) * dt / 2);
			}
	
			particles[i].x = x + (vi * dt) + (ai * dt * dt / 2);  
			particles[i].y = y + (vj * dt) + (aj * dt * dt / 2);			
			
			pv.vi_old = vi;
			pv.vj_old = vj;
			
			pv.ai_old = ai;
			pv.aj_old = aj;
			pv.ai = 0.0;
			pv.aj = 0.0;
		}
	}
	
	protected static void noaliasArrayCopy(final noalias Particle[] src, final noalias Particle[] dest)
	{
		for (int i = 0; i < src.length; i++)
		{
			noalias Particle p = new Particle();
		
			p.x = src[i].x;
			p.y = src[i].y;
			p.m = src[i].m;
			
			dest[i] = p;
		}
	}
	
	protected static String noaliasArrayToString(final noalias Particle[] ps)
	{
		String m = "[";
	
		for (int i = 0; i < ps.length; i++)
		{
			m += ps[i].toString();
			
			if (i < (ps.length - 1))
			{
				m += ", ";
			}
		}
		
		m += "]";
		
		return m;
	}
}
