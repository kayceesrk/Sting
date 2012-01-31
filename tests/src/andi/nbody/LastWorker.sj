//$ bin/sessionjc -cp tests/classes/ tests/src/andi/nbody/LastWorker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmark1.FClient localhost 4444 2 100 1

/**
 * 
 * @author Andi
 *
 */
package andi.nbody;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class LastWorker{
	
	private final noalias protocol gather_scatter
	{
		?(int[]).!<int[]>
	}
	private final noalias protocol gather_scatter2
	{
		?(Double).!<Double>
	}
	private final noalias protocol nbody_s
	{
		sbegin.@(gather_scatter).?[?[?(Particle[])]*]*.@(gather_scatter2)
				
	}
	private final noalias protocol nbody_C
	{
		cbegin.!<Particle[]>
	}
	private final int MAX_PARTICLES = 4000;
	private final int MAX_P = 128;
	private final int size = 4;

	public void run(int npart, int size, String right_host, int right_port, int left_port){
		
		// particles on ALL nodes
		Particle[] particles = new Particle[npart];
		// particles velocity
		ParticleV[] pv = new ParticleV[npart]; 
		
		Particle[] sendBuf = new Particle[MAX_PARTICLES];
		Particle[] recvBuf = null;
		 
		// number of particles assigned to every process
		int[] counts = new int[size];
		
		final noalias SJService right_c = SJService.create(nbody_C, right_host, right_port);
		
		final noalias SJServerSocket left_ss;
		
		try(left_ss){
					
			left_ss = SJServerSocketImpl.create(nbody_s, left_port);	
			final noalias SJSocket left;			
					
			try(left){
			
				left = left_ss.accept();		
				
				int[] full_counts = (int[])left.receive();
				full_counts[size - 1] = npart;	
				left.send(full_counts);
				
				System.arraycopy(full_counts, 0, counts, 0, size);
				
				initParticles(particles, pv, npart); 
				
				double sim_t = 0.0;
				/*binary session types define a master-workers model of programming;
				 e.g. iterations and conditionals*/
									
				left.inwhile(){
					
					double max_f, max_f_seg = 0.0;
								
					System.arraycopy(particles, 0, sendBuf, 0, npart);
					int pipe = 0;
					left.inwhile(){
					
						final noalias SJSocket right;		
						try(right){ 
						
							right = right_c.request();			
							right.send(sendBuf);
						}finally{}
						
						if(pipe == 0)	
							//compute forces for local particles
							max_f_seg = computeForces(particles, sendBuf, pv, npart, npart);
						else
							//compute forces for particles in other machines
							max_f_seg = computeForces(particles, sendBuf, pv, npart, counts[size - (pipe + 1)]);
						
						recvBuf = (Particle[])left.receive();
							
						if(max_f_seg > max_f)
							max_f = max_f_seg;
							
						pipe ++;
						System.arraycopy(recvBuf, 0, sendBuf, 0, counts[size - (pipe + 1)]);			
					}
					max_f_seg = computeForces(particles, sendBuf, pv, npart, counts[size - (pipe + 1)]);
					
					if(max_f_seg > max_f)
						max_f = max_f_seg;
								
					double dt_est = computeNewPos(particles, pv, npart, max_f);
								
					double dt_est_part = ((Double)left.receive()).doubleValue();
					if(dt_est > dt_est_part)
						dt_est = dt_est_part;
					left.send(new Double(dt_est));
					double dt_old;
					if(dt_est < 0.001){
									
						dt_old = 0.001;
						double dt = dt_est;
								
					}else if(dt_est > 4.0*0.001){
								
						dt_old = 0.001;
						double dt = 2.0 * 0.001; 
					}
								
					sim_t += dt_old;
				}
			}finally{}
		}finally{}
	}
	/*
	public static void main(String args[]){
	
		int npart = Integer.parseInt(args[1]) / size;
		
		if(npart*size <= MAX_PARTICLES)
		
			run(npart, size, );
		else
			System.out.println("Too many particles.")
	}*/
	
	void initParticles(Particle[] particles, ParticleV[] pv, int npart){
	
		for(int i = 0; i < npart; i++){
		
			particles[i].x = Math.random();
			particles[i].y = Math.random();
			particles[i].z = Math.random();
			particles[i].mass = 1.0;
			
			pv[i].xold = particles[i].x;
			pv[i].yold = particles[i].y;
			pv[i].zold = particles[i].z;
			pv[i].fx = 0;
			pv[i].fy = 0;
			pv[i].fz = 0;
		}
	}
	
	double computeForces(Particle[] particles, Particle[] others, ParticleV[] pv, int npart, int counts){

		double max_f = 0.0;	
		double rmin;
		
		for(int i = 0; i < npart; i++){
		
			double xi, yi, mi, rx, ry, mj, r, fx, fy;
			rmin = 100.0;
			xi = particles[i].x;
			yi = particles[i].y;
			fx = 0.0;
			fy = 0.0;
			
			for(int j = 0; j < counts; j++){
			
				rx = xi - others[j].x;
				ry = yi - others[j].y;
				mj = others[j].mass;
				r = rx*rx + ry*ry;
				
				if(r < rmin)
					rmin = r;
				r = r * Math.sqrt(r);
				fx -= mj * rx / r;
				fy -= mj * ry / r;
			}
			pv[i].fx += fx;
			pv[i].fy += fy;
			
			fx = Math.sqrt(fx*fx + fy*fy)/rmin;
			
			if(fx > max_f) max_f = fx;
		}	
		
		return max_f;
	}
	
	double computeNewPos(Particle[] particles, ParticleV[] pv, int npart, double max_f){
	
		double a0, a1, a2;
		double dt_old = 0.001, dt = 0.001;
		double dt_est;
		
		a0 = 2.0 / (dt * (dt + dt_old));
		a2 = 2.0 / (dt_old * (dt + dt_old));
		a1 = -(a0 + a2);
		
		for(int i = 0; i < npart; i++){
		
			double xi = particles[i].x;
			double yi = particles[i].y;
			
			particles[i].x = (pv[i].fx - a1*xi - a2*pv[i].xold)/a0;
			particles[i].y = (pv[i].fy - a1*yi - a2*pv[i].yold)/a0;
			
			pv[i].xold = xi;
			pv[i].yold = yi;
			pv[i].fx = 0;
			pv[i].fy = 0;
		}
		
		dt_est = 1.0/Math.sqrt(max_f);
		
		if(dt_est < 1.0*Math.exp(-6))
		
			dt_est = 1.0*Math.exp(-6);
			
		return dt_est;	
	}
}