//$ bin/sessionjc -cp tests/classes/ tests/src/places/onedimjacobi/Sequential.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.onedimjacobi.Sequential 6

package places.onedimjacobi;

import java.io.*;
import java.util.Arrays;

public class Sequential
{
	private static final int MAX_ITERATIONS = 100000;	
	
	private void run(int size)
	{
		// Build its sub-grid.
		double[][] u = new double[size + 2][size + 2];
		
		// Sub-grid next iterations with the same dimension as u.
		double[][] newu = new double[size + 2][size + 2]; 
						
		// Initialise u, unew, f.						
		init(u, newu, size, size);				 
		
		double diff = 1.0;
		double valmx = 1.0;		
		
		int iterations = 1;
		
		//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		//try { br.readLine(); } catch(IOException ioe) { }
		
		while ((diff / valmx) >= (1.0 * Math.pow(10, -5)) && iterations <= MAX_ITERATIONS)
		{	
			//try { br.readLine(); } catch(IOException ioe) { }
			
			//System.out.println("\nIteration: " + iterations);

			diff = 0.0;
			valmx = 0.0;
						
			// Jacobi iterations.						
			for (int i = 1; i < size + 1; i++)
			{
				for (int j = 1; j < size + 1; j++)
				{								
					newu[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
					
					diff = Math.max(diff, Math.abs(newu[i][j] - u[i][j]));
					valmx = Math.max(valmx, Math.abs(newu[i][j]));
				}
			}
			
			double[][] tmp = u;
			u = newu;
			newu = tmp;
			
			iterations++;
			
			//printMatrix(u);									
		}
		
		System.out.println("\nFinal result: ");
		
		printMatrix(u);
		
		if (iterations > MAX_ITERATIONS)
		{
			System.out.println("[Master] Jacobi iterations did not converge in: " + iterations + " iterations.");
		}						
		else
		{
			System.out.println("[Master] Jacobi converged after: " + iterations + " iterations.");
		}		
	}
	
	public static void main(String[] args)
	{
		int size = Integer.parseInt(args[0]);
			
		Sequential s = new Sequential();
		
		s.run(size);
	}
	
	private void init(double[][] u, double[][] newu, int rows, int size)
	{	
		/*for (int i = 0; i < rows + 2; i++) // Unnecessary.
		{	
			for (int j = 0; j < size + 2; j++)
			{			
				u[i][j] = 0.0;
				newu[i][j] = 0.0;
			}
		}*/			
		
		double sized = (double) size;
		
		// Initialize boundaries.
		for (int i = 1; i < rows + 2; i++)
		{
			double d = (double) i; 
			
			u[i][0] = Math.sin(Math.PI * d / (sized + 1.0));
			newu[i][0] = Math.sin(Math.PI * d / (sized + 1.0)); // Unnecessary.
			
			u[i][size + 1] = Math.sin(Math.PI * d / (sized + 1.0)) * Math.exp(-1.0 * Math.PI);
			newu[i][size + 1] = Math.sin(Math.PI * d / (sized + 1.0)) * Math.exp(-1.0 * Math.PI);
		}

		for (int j = 0; j < size + 2; j++) // FIXME: why initialised to 0?
		{		
			u[rows + 1][j] = 0.0;
			newu[rows + 1][j] = 0.0;
		}		
		
		System.out.println("Matrix for Seqeuntial: ");
		  
		printMatrix(u);
	}
	
	private void printMatrix(double[][] u)
	{
		for (int i = 0; i < u.length; i++)
		{
			System.out.println(Arrays.toString(u[i]));	
		}
	}
}
