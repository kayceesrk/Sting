//$ bin/sessionjc -cp tests/classes/ tests/src/onedimjacobi/noaliaz/Master.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ onedimjacobi.noaliaz.Master 4444 localhost 4442 localhost 4443

package onedimjacobi.noaliaz;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Master
{
	private final noalias protocol p_mc { sbegin.?(int).!<double[][]> }

	private final noalias protocol matrix_size { !<int> }
	private final noalias protocol stopping_condition { ?(Double).?(Double) }
	private final noalias protocol ghost_points { !<double[]>.?(double[]) }
	private final noalias protocol partial_result { ?(double[][]) }	
	private final noalias protocol p_mw 
	{ 
		cbegin
		.@(matrix_size)
		.![
		  @(ghost_points)
		  .@(stopping_condition)
		]*
		.@(partial_result)
	}
		
	private static final int MAX_ITERATIONS = 100000;
	
	public void run(int port_m, String host_n, int port_n, String host_s, int port_s)
	{
		/* Socket that communicates with Client. */
		final noalias SJServerSocket ss;
		
		/* Sockets that communicates with neighbours. */
		final noalias SJService c_n = SJService.create(p_mw, host_n, port_n);
		final noalias SJService c_s = SJService.create(p_mw, host_s, port_s);
	
		try (ss)
		{		
			/* Create and set up the server socket. */			
			ss = SJServerSocketImpl.create(p_mc, port_m);
			
			while (true)
			{
				final noalias SJSocket cm;
						
				try (cm)
				{				
					/* Accept the connection from client. */					
					cm = ss.accept();
		
					// size of the problem.
					int size = cm.receiveInt(); 
				
					int rows = size / 3; // Possible rounding errors, not too important. 
					
					// System.out.println("[Master] Number of rows: " + rows);
					
					final noalias SJSocket mn, ms;
					
					try (cm, mn, ms)
					{								
						/* Set up the connection with neighbours. */						
						mn = c_n.request();
					  ms = c_s.request();
						
					  // Send to the first and second neighbor the size of the problem.
						<mn, ms>.send(size);
						
						// Build its sub-grid.
						double[][] u = new double[rows + 2][size + 2];
						
						// Sub-grid next iterations with the same dimension as u.
						double[][] newu = new double[rows + 2][size + 2]; 
										
						// Initialise u, unew, f.						
						init(u, newu, rows, size);
					
						// Ghost zone of neighbors.					
						/*double[] border_n = new double[size]; 
						double[] border_s = new double[size]; */
						
						noalias double[] ghost_n = new double[size]; 
						noalias double[] ghost_s = new double[size]; 
						 
						double diff = 1.0;
						double valmx = 1.0;
						
						//System.out.println("[Master] Error tolerance: " + (1.0 * Math.pow(10, -5)));						 						
						
						int iterations = 1;
						
						//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
						//try { br.readLine(); } catch(IOException ioe) { }
						
						<mn, ms>.outwhile((diff / valmx) >= (1.0 * Math.pow(10, -5)) && iterations <= MAX_ITERATIONS)
						{						
							//try { br.readLine(); } catch(IOException ioe) { }
							
							//System.out.println("\nIteration: " + iterations);
			
							diff = 0.0;
							valmx = 0.0;
							
							// Jacobi iterations.						
							for(int i = 1; i < rows + 1; i++)
							{
								for(int j = 1; j < size + 1; j++)
								{								
									newu[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
									
									if (i == 1)
									{
										ghost_n[j - 1] = newu[i][j];
									}
									
									if (i == rows) // For size < 6, there is only one row.
									{
										ghost_s[j - 1] = newu[i][j];
									}
									
									diff = Math.max(diff, Math.abs(newu[i][j] - u[i][j]));
									valmx = Math.max(valmx, Math.abs(newu[i][j]));
								}								
							}
						
							// Send the ghost zones of neighbours.							
							/*for(int k = 0; k < size; k++)
							{								
								border_n[k] = newu[1][k + 1];
							}																								
							
							for(int k = 0; k < size; k++)	
							{
								border_s[k] = newu[rows][k + 1];
							}*/
							
							/*double[] border_n = new double[size]; // Because of stupid oos caching.
							double[] border_s = new double[size];*/							
							
							/*System.arraycopy(newu[1], 1, border_n, 0, size);
							System.arraycopy(newu[rows], 1, border_s, 0, size);						
							
							mn.send(border_n); 	 
							ms.send(border_s);*/
							
							/*for (int z = 0; z < size; z++)
							{
								ghost_n[z] = newu[1][z+1];
								ghost_s[z] = newu[rows][z+1];
							}*/
							
							mn.send(ghost_n); 	 
							ms.send(ghost_s);
						
							// Receive ghost zones from neighbours.							
							/*double[]*/ ghost_n = (double[]) mn.receive();
							/*double[]*/ ghost_s = (double[]) ms.receive();
							
							// Copy ghost zones in newu.						
							/*for (int k = 0; k < ghost_n.length; k++)
							{
								newu[0][k + 1] = ghost_n[k];
							}
														
							for(int k = 0; k < ghost_s.length; k++)
							{								
								newu[rows + 1][k+1] = ghost_s[k];
							}*/
					
							/*System.arraycopy(ghost_n, 0, newu[0], 1, size);
							System.arraycopy(ghost_s, 0, newu[rows + 1], 1, size);*/
							
							noaliasArrayCopy(ghost_n, newu[0], 1);
							noaliasArrayCopy(ghost_s, newu[rows + 1], 1);
							
							// Update u with newu.							
							/*for (int k = 1; k < u.length - 1; k++) // FIXME: should copy from 0 to u.length, otherwise the ghost values we just received above are lost.
							{
								for(int l = 1; l < u[0].length - 1; l++)
								{
									u[k][l] = newu[k][l];
								}
							}*/
							
							double[][] tmp = u;
							u = newu;
							newu = tmp;
																							
							// Computing newerror.								
							diff = Math.max(diff, ((Double) mn.receive()).doubleValue());
							valmx = Math.max(valmx, ((Double) mn.receive()).doubleValue());
							
							diff = Math.max(diff, ((Double) ms.receive()).doubleValue());
							valmx = Math.max(valmx, ((Double) ms.receive()).doubleValue());
							
							//System.out.println("[Master] Diff: " + diff + ", Valmx: " + valmx);
															
							if (iterations == 1) // ?
							{
								diff = 1.0; valmx = 1.0;
							}								
							
							iterations++;		
							
							//printMatrix(u);								
						}
							
						if (iterations > MAX_ITERATIONS)
						{
							System.out.println("[Master] Jacobi iterations did not converge in: " + iterations + " iterations.");
						}						
						else
						{
							System.out.println("[Master] Jacobi converged after: " + iterations + " iterations.");
						}
						
						double[][] w1 = (double[][]) mn.receive();
						double[][] w2 = (double[][]) ms.receive();
						
						double[][] result = new double[size][size];
						
						for (int i = 0; i < rows; i++)
						{
							for(int j = 0; j < size; j++)
							{
								result[i][j] = w1[i + 1][j + 1];
							}
						}
						
						for (int i = rows; i < 2 * rows; i++)
						{
							for(int j = 0; j < size; j++)
							{
								result[i][j] = u[i - rows + 1][j + 1];
							}
						}
							
						for (int i = 2 * rows; i < size; i++)
						{
							for(int j = 0; j < size; j++)
							{
								result[i][j] = w2[i - 2 * rows + 1][j + 1];
							}
						}								
							
						cm.send(result);
					}
					finally
					{							
						
					}
				}
				finally
				{
					
				}				
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Master] Non-dual behavior: " + ise);
		} 
		catch (SJIOException sioe)
		{
			System.err.println("[Master] Communication error: " + sioe);				
		} 
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[Master] Class error: " + cnfe);
		}
		finally
		{
			
		}	
	}

	public static void main(String args[]){
		
		Master master = new Master();
	
		master.run(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]), args[3], Integer.parseInt(args[4]));

	}
	
	private void init(double[][] u, double[][] newu, int rows, int size)
	{	
		for (int i = 0; i < rows + 2; i++) // Unnecessary.
		{	
			for (int j = 0; j < size + 2; j++)
			{			
				u[i][j] = 0.0;
				newu[i][j] = 0.0;
			}
		}			
		
		double sized = (double) size;
		
		// Initialize boundaries.
		for (int i = 0; i < rows + 2; i++) 
		{
			double d = (double) i + rows; // Can use rows here because the left over rows from non-multiple of three size are moved to WorkerS. 
			
			u[i][0] = Math.sin(Math.PI * d / (sized + 1.0));
			newu[i][0] = Math.sin(Math.PI * d / (sized + 1.0)); // Unnecessary.
			
			u[i][size + 1] = Math.sin(Math.PI * d / (sized + 1.0)) * Math.exp(-1.0 * Math.PI);
			newu[i][size + 1] = Math.sin(Math.PI * d / (sized + 1.0)) * Math.exp(-1.0 * Math.PI);
		}
		
		//System.out.println("Matrix for Master: ");		  
		//printMatrix(u);
	}
	
	protected static void noaliasArrayCopy(final noalias double[] src, double[] dest, int offset)
	{
		for (int i = 0; i < src.length; i++)
		{
			dest[i + offset] = src[i];
		}
	}
	
	private void printMatrix(double[][] u)
	{
		for (int i = 0; i < u.length; i++)
		{
			System.out.println(Arrays.toString(u[i]));	
		}
	}	
}
