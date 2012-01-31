//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark1/FServer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmark1.FServer 4444
/**
 * 
 * @author Andi
 *
 */
package benchmark1;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.BigObject;
import benchmark1.BinaryTree;

public class FServer 
{
	
	private final noalias protocol p 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
	}	

	private ServerSocket dummy = null;
	
	public FServer(int port) throws Exception 
	{					
		
		final noalias SJServerSocket ss;
		try(ss){
			ss = SJServerSocketImpl.create(p, port);
			BinaryTree theTree;
			while (true) 
			{			
			
				final noalias SJSocket ds;
				try(ds){
						
					//System.out.println("Run: " + run++);
					ds = ss.accept();
					
					ds.inwhile(){
						 theTree = (BinaryTree)ds.receive();
						 //theTree.print();
						 theTree.inc();
						 ds.send(theTree);
						}
					
				}finally{}
				
				//System.out.println("Server dummy session done");		
				
				final noalias SJSocket s;
				
				try (s) 
				{	s = ss.accept(); // Actual run.
					//System.out.println("Server connection established");
					s.inwhile(){
					 	theTree = (BinaryTree)s.receive();
					 	//theTree.print();
					 	theTree.inc();
					 	s.send(theTree);
					}
				}
				finally 
					{ 
	
					}					
			}
			
		}catch(SJIncompatibleSessionException ise)
		{
			System.err.println( " incompatible client: " + ise);
		}
		catch(SJIOException ioe)
		{
			System.err.println( " IO error: " + ioe);			
		}finally{}			
	}

	public static void main(String[] args) throws Exception 
	{
		new FServer(Integer.parseInt(args[0]));
	}
}
