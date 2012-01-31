//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark2/c/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark2.c.Kill localhost 8888

/**
 * 
 */
package pldi.benchmarks.benchmark2.c;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

/**
 * The same as benchmark1.a.Kill.
 */
public class Kill 
{	
	public static final int KILL_PORT_ADJUST = 2;
	
	private static final noalias protocol p { cbegin }
	
	public static void main(String args[]) throws Exception 
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
					
		final noalias SJService c = SJService.create(p, server, port + KILL_PORT_ADJUST);

		final noalias SJSocket s;			
			
		try (s) 
		{
			s = c.request(); 	
		}		
		catch (Exception x)
		{
			x.printStackTrace();
		}		
		finally 
		{
			 		
		}		
	}
}
