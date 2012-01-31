//$ bin/sessionjc -cp tests/classes/ tests/src/nbody/Sequential.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ nbody.Sequential false 1 1

package nbody;

import java.util.*;

import nbody.ordinary.*;

public class Sequential
{
	public void run(boolean debug, int numParticles, int reps)
	{
		Particle[] particles = new Particle[numParticles];
		ParticleV[] pvs = new ParticleV[numParticles];
		
		initParticles(debug, particles, pvs);
		
		int i = 0;
		
		long timeStarted = 0;		
		long timeFinished = 0;			
							
		timeStarted = System.nanoTime();		
		
		for ( ; i < reps; i++)
		{
			if (debug)
			{
				System.out.println("\nIteration: " + i);
				System.out.println("Particles: " + Arrays.toString(particles));
			}
				
			nbody.ordinary.Worker.computeForces(particles, particles, pvs);		
			nbody.ordinary.Worker.computeNewPos(particles, pvs, i);
		}
		
		timeFinished = System.nanoTime();
				
		System.out.println("time = " + (timeFinished - timeStarted) / 1000 + " micros");		
		
		if (debug)
		{
			System.out.println("\nIteration: " + i);
			System.out.println("Particles: " + Arrays.toString(particles));
		}
	}

	private void initParticles(boolean debug, Particle[] particles, ParticleV[] pvs)
	{
		for(int i = 0; i < particles.length; i++)
		{		
			Particle p = new Particle();
			
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
	
	public static void main(String args[])
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int numParticles = Integer.parseInt(args[1]);
		int reps = Integer.parseInt(args[2]);
		
		Sequential s = new Sequential(); 
			
		s.run(debug, numParticles, reps);
	}	
}
