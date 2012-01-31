//$ bin/sessionjc -cp tests/classes/ tests/src/thesis/benchmark/bmark3/Common.sj -d tests/classes/

package thesis.benchmark.bmark3;

import sessionj.runtime.SJProtocol;

import thesis.benchmark.bmark3.Particle;
import thesis.benchmark.bmark3.ParticleV;

public class Common
{
	public static final int ITERATION_DELAY = 500; // Millis
	
	//public static final double G = 6.674 * Math.pow(10, -11);
	public static final double G = 1.0;
		
	public static protocol NBODY_SERVER sbegin.?(boolean).?(Particle[]).?(ParticleV[]).!<Particle[]> // boolean ignored except for LastWorker
	public static protocol NBODY_CLIENT ^(NBODY_SERVER)
	
	public static protocol LINK_SERVER sbegin.!<int>.?[?[?(Particle[])]*]* // Server socket is on our s_l side: s_l guy connects to us		
	public static protocol LINK_CLIENT ^(LINK_SERVER)                     // Client socket is on out s_r side: we connect to the s_r guy
	
	public static final void computeForces(Particle[] particles, Particle[] current, ParticleV[] pvs)
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
					ai += ((ri < 0) ? -1 : 1) * Common.G * m / (ri * ri);
					
					//System.out.println("\n1: " + pvs[ours].ai + ", " + ai + "\n");
				}
				
				if (rj != 0)
				{
					aj += ((rj < 0) ? -1 : 1) * Common.G * m / (rj * rj);					
				}
			}
						
			pvs[ours].ai += ai;						
			pvs[ours].aj += aj;
		}	
	}
	
	public static final void computeNewPos(Particle[] particles, ParticleV[] pvs, int step)
	{
		double dt = 1.0;			
		for(int i = 0; i < particles.length; i++)
		{
			Particle p = particles[i]; 
			ParticleV pv = pvs[i];
			
			double x = p.x;
			double y = p.y;			
			
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
	
			p.x = x + (vi * dt) + (ai * dt * dt / 2);  
			p.y = y + (vj * dt) + (aj * dt * dt / 2);			
			
			pv.vi_old = vi;
			pv.vj_old = vj;
			
			pv.ai_old = ai;
			pv.aj_old = aj;
			pv.ai = 0.0;
			pv.aj = 0.0;
		}
	}	
	
	public static final void debugPrintln(boolean debug, String m)
	{
		if (debug)
		{
			System.out.println(m);
		}
	}
}
