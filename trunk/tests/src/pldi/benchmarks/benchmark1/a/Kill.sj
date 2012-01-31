//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/a/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.a.Kill localhost 8888

/**
 * 
 */
package pldi.benchmarks.benchmark1.a;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

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
