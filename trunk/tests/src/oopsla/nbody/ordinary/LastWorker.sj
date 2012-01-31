//$ bin/sessionjc -cp tests/classes/ tests/src/nbody/ordinary/LastWorker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ nbody.ordinary.LastWorker false 4444 localhost 4442 1

package nbody.ordinary;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import nbody.Particle;
import nbody.ParticleV;

public class LastWorker
{
	private final static int MAX_PARTICLES = 4000;
	
	private final noalias protocol ps_nbody
	{
		sbegin.!<int>.?[?[?(Particle[])]*]*				
	}
	
	private final noalias protocol pc_ring
	{
		cbegin.!<Particle[]>
	}
			 
	public void run(boolean debug, int port_l, String host_r, int port_r, int numParticles)
	{
		// The particles.
		Particle[] particles = new Particle[numParticles];
		
		// The particles' velocities.
		ParticleV[] pvs = new ParticleV[numParticles]; 	
	
		final noalias SJService right_c = SJService.create(pc_ring, host_r, port_r);
		
		final noalias SJServerSocket ss_l;
		
		try(ss_l)
		{			
			ss_l = SJServerSocketImpl.create(ps_nbody, port_l);
			
			final noalias SJSocket s_l;			
					
			try(s_l)
			{			
				s_l = ss_l.accept();		
								
				s_l.send(1);
				
				initParticles(debug, particles, pvs); 							
				
				int i = 0;
				
				s_l.inwhile()
				{				
					if (debug)
					{
						System.out.println("\nIteration: " + i);
						System.out.println("Particles: " + Arrays.toString(particles));
					}					
				
					Particle[] current = new Particle[numParticles];					
					
					System.arraycopy(particles, 0, current, 0, numParticles);
					
					s_l.inwhile()
					{					
						final noalias SJSocket s_r;
						
						try(s_r)
						{ 						
							s_r = right_c.request();
							
							s_r.send(current);
						}
						finally
						{							
							
						}
						
						Worker.computeForces(particles, current, pvs);
						
						current = (Particle[]) s_l.receive();
					}
									
					Worker.computeForces(particles, current, pvs);
					
					Worker.computeNewPos(particles, pvs, i);	
					
					i++;
				}
				
				if (debug)
				{
					System.out.println("\nIteration: " + i);
					System.out.println("Particles: " + Arrays.toString(particles));
				}	
			}
			finally
			{
				
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Client] Non dual-behavior: " + ise);
		}
		catch (SJIOException sioe)
		{
			System.err.println("[Client] Communication error: " + sioe);				
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[Client] Class error: " + cnfe);
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
		
		if (numParticles <= MAX_PARTICLES)
		{		
			LastWorker lw = new LastWorker();
			
			lw.run(debug, port_l, host_r, port_r, numParticles);
		}
		else
		{
			System.out.println("Too many particles.");
		}
	}
	
	private void initParticles(boolean debug, Particle[] particles, ParticleV[] pvs)
	{
		for(int i = 0; i < particles.length; i++)
		{		
			Particle p = new Particle();
			
			if (debug)
			{
				p.x = particles.length + i;
				p.y = particles.length + i;
				p.m = 1.0;
			}
			else
			{
				p.x = 10.0 * Math.random();
				p.y = 10.0 * Math.random();
				p.m = 10.0 * Math.random();
			}
			
			/*p.x = i + 2;
			p.y = i + 2;						
			p.m = i + 2;*/
			
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
}
