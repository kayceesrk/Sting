//$ bin/sessionjc -cp tests/classes/ tests/src/nbody/noaliaz/FirstWorker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ nbody.noaliaz.FirstWorker false 4442 localhost 4443 1 1

package nbody.noaliaz;

import java.util.Arrays;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import nbody.Particle;
import nbody.ParticleV;

public class FirstWorker
{	
	private final static int MAX_PARTICLES = 4000;
	private final static int MAX_PROCESSORS = 128;	
		
	private final noalias protocol pc_nbody
	{
		cbegin.?(int).![![!<Particle[]>]*]*
	}
	
	private final noalias protocol ps_ring
	{
		sbegin.?(Particle[])
	}	

	public void run(boolean debug, int port_l, String host_r, int port_r, int numParticles, /*int numProcessors*/ int reps)
	{
		// The particles.
		final noalias Particle[] particles = new Particle[numParticles];
		
		// The particles' velocities.
		ParticleV[] pvs = new ParticleV[numParticles]; 
	
		int numProcessors = 0;
		
		final noalias SJService c_r = SJService.create(pc_nbody, host_r, port_r);
		final noalias SJSocket s_r;
		
		final noalias SJServerSocket ss_l;
		
		try (ss_l)
		{						
			ss_l = SJServerSocketImpl.create(ps_ring, port_l);		
				
			try (s_r)
			{ 				
				long timeStarted = 0;		
				long timeFinished = 0;			
			
				s_r = c_r.request();
							
				timeStarted = System.nanoTime();
				
				numProcessors = s_r.receiveInt() + 1;
				
				if (debug)
				{
					System.out.println("Number of processors: " + numProcessors);
				}
				
				/* Generate initial values. */
				initParticles(debug, particles, pvs);
												
				int i = 0;
				
				s_r.outwhile(i < reps)
				{	
					if (debug) // For i == 0, prints initialisation values.
					{
						System.out.println("\nIteration: " + i);
						System.out.println("Particles: " + Worker.noaliasArrayToString(particles));
					}	
					
					noalias Particle[] current = new Particle[numParticles];
					
					Worker.noaliasArrayCopy(particles, current);
					
					int j = 0;
					
					s_r.outwhile(j < (numProcessors - 1))
					{									
						Worker.computeForces(particles, current, pvs);					
					
						s_r.send(current);							
							
						final noalias SJSocket s_l;
						
						try (s_l)
						{
							s_l = ss_l.accept();	
								
							current = (Particle[]) s_l.receive();
						}
						finally
						{
														
						}		
						
						j++;
					}													
									
					Worker.computeForces(particles, current, pvs);
										
					Worker.computeNewPos(particles, pvs, i);
										
					i++;
				}

				timeFinished = System.nanoTime();
				
				System.out.println("time = " + (timeFinished - timeStarted) / 1000 + " micros");
				
				if (debug)
				{
					System.out.println("\nIteration: " + i);
					System.out.println("Particles: " + Worker.noaliasArrayToString(particles));
				}				
			}
			finally
			{					
				
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[FirstWorker] Non-dual behavior: " + ise);
		}
		catch (SJIOException sioe)
		{
			System.err.println("[FirstWorker] Communication error: " + sioe);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[FirstWorker] Class error: " + cnfe);			
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
		//int numProcessors = Integer.parseInt(args[5]);		
		int reps = Integer.parseInt(args[5]);
		
		if (numParticles <= MAX_PARTICLES/* && numParticles <= MAX_PROCESSORS*/)
		{	
			FirstWorker fw = new FirstWorker(); 
			
			fw.run(debug, port_l, host_r, port_r, numParticles, reps);
		}
		else
		{
			System.out.println("Too many particles: " + numParticles);
		}
	}
	
	private void initParticles(boolean debug, final noalias Particle[] particles, ParticleV[] pvs)
	{
		for(int i = 0; i < particles.length; i++)
		{		
			noalias Particle p = new Particle();
			
			if (debug)
			{
				p.x = i;
				p.y = i;
				p.m = 1.0;
			}
			else
			{
				p.x = 10.0 * Math.random();
				p.y = 10.0 * Math.random();
				p.m = 10.0 * Math.random();
			}
			
			/*p.x = i + 1;
			p.y = i + 1;			
			p.m = i + 1;*/
			
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
