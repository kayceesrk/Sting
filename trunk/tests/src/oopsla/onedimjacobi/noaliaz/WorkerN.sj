//$ bin/sessionjc -cp tests/classes/ tests/src/onedimjacobi/noaliaz/WorkerN.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ onedimjacobi.noaliaz.WorkerN 4442

package onedimjacobi.noaliaz;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class WorkerN
{	
	private final noalias protocol matrix_size { ?(int) }
	private final noalias protocol ghost_points { ?(double[]).!<double[]> }
	private final noalias protocol stopping_condition { !<Double>.!<Double> }
	private final noalias protocol result { !<double[][]> }	
	private final noalias protocol p_wm 
	{
		sbegin
		.@(matrix_size)
		.?[
		  @(ghost_points)
		  .@(stopping_condition)
		]*
		.@(result)
	}
	
	public void run(int port_w)
	{
		/* Socket that communicates with Master. */
		final noalias SJServerSocket ss;
	
		try (ss)
		{		
			/* Create and set up the server socket. */			
			ss = SJServerSocketImpl.create(p_wm, port_w);
			
			while (true)
			{
				final noalias SJSocket wm;
				
				try (wm)
				{				
					/* Accept the connection from Master. */			
					wm = ss.accept();
		
					// Size of the problem.
					int size = wm.receiveInt(); 
					
					int rows = size / 3;
					
					//System.out.println("[WorkerN] Rows: " + rows);
						
					// Build its sub-grid.
					double[][] u = new double[rows + 2][size + 2];
						
					// Sub-grid next iteration with the same dimension as u.
					double[][] newu = new double[rows + 2][size + 2]; 
										
					// Initialise u, unew.					
					init(u, newu, rows, size);
					
					// Ghost zone of neighbors.		 
					//double[] border_m = new double[size];  
					
					noalias double[] ghost_m = new double[size];
					noalias double[] prev;  
					
					double diff, valmx;
					
					wm.inwhile()
					{
						diff = 0.0;
						valmx = 0.0;
						
						// Jacobi iteration.						
						for (int i = 1; i < rows + 1; i++ )
						{	
							for(int j = 1; j < size + 1; j++)
							{							
								newu[i][j] = (u[i - 1][j] + u[i + 1][j] + u[i][j - 1] + u[i][j + 1]) / 4.0;
								
								if (i == rows)
								{
									ghost_m[j - 1] = newu[i][j];
								}
								
								diff = Math.max(diff, Math.abs(newu[i][j] - u[i][j]));
								valmx = Math.max(valmx, Math.abs(newu[i][j]));
							}
						}
						
						/*for (int z = 0; z < size; z++)
						{
							ghost_m[z] = newu[rows][z+1];
						}*/
						
						prev = ghost_m;
						
						// Receive ghost zones from neighbours.						
						/*double[]*/ ghost_m = (double[]) wm.receive();
						
						// Send the ghost zones of neighbours.							
						/*for (int i = 0; i < size; i++)	
						{						
							border_m[i] = newu[rows][i + 1];
						}*/
							 
						//double[] border_m = new double[size]; // Because of stupid oos caching.
						
						/*System.arraycopy(newu[rows], 1, border_m, 0, size);
						
						wm.send(border_m);*/
	
						wm.send(prev);
	
						// Copy ghost zones in newu.						
						/*for (int i = 0; i < ghost_m.length; i++)
						{
							newu[rows + 1][i + 1] = ghost_m[i];
						}*/
						
						//System.arraycopy(ghost_m, 0, newu[rows + 1], 1, size);

						Master.noaliasArrayCopy(ghost_m, newu[rows + 1], 1);

						// Computing newerror.						
						wm.send(new Double(diff));
						wm.send(new Double(valmx));
						
						// Update u with newu.						
						/*for (int i = 1; i < u.length - 1; i++) // FIXME: should copy from 0 to u.length, otherwise the ghost values we just received above are lost.
						{
							for(int j = 1; j < u[0].length - 1; j++)
							{
								u[i][j] = newu[i][j];
							}
						}*/
						
						double[][] tmp = u;
						u = newu;
						newu = tmp;
						
						/*System.out.println();
						printMatrix(u);*/
					}
							
					wm.send(u);					
				}
				finally
				{
					
				}
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Service] Non-dual behavior: " + ise);
		}
		catch (SJIOException sioe)
		{
			System.err.println("[Service] Communication error: " + sioe);				
		}
		catch (ClassNotFoundException cnfe)
		{
			System.err.println("[Service] Class error: " + cnfe);
		}
		finally
		{
		
		}	
	}

	public static void main(String args[])
	{
		int port_n = Integer.parseInt(args[0]);
		
		WorkerN n = new WorkerN();
	
		n.run(port_n);
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
			double d = (double) i; 
			
			u[i][0] = Math.sin(Math.PI * d / (sized + 1.0));
			newu[i][0] = Math.sin(Math.PI * d / (sized + 1.0));
			
			u[i][size + 1] = Math.sin(Math.PI * d / (sized + 1.0)) * Math.exp(-1.0 * Math.PI);
			newu[i][size + 1] = Math.sin(Math.PI * d / (sized + 1.0)) * Math.exp(-1.0 * Math.PI);
		}

		for (int j = 0; j < size + 2; j++) // Unnecessary (row 0 gives sin 0 anyway).
		{		
			u[0][j] = 0.0;
			newu[0][j] = 0.0;
		}
		
		//System.out.println("Matrix for WorkerN: ");		  		  
		//printMatrix(u);
	}
	
	private void printMatrix(double[][] u)
	{
		for (int i = 0; i < u.length; i++)
		{
			System.out.println(Arrays.toString(u[i]));	
		}
	}	
}
