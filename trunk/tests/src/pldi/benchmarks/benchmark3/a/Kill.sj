//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark3/a/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3.a.Kill localhost 8888

/**
 * 
 */
package pldi.benchmarks.benchmark3.a;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

public class Kill 
{	
	private static final noalias protocol p { cbegin }
	
	public static void main(String args[]) throws Exception 
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
					
		final noalias SJService c = SJService.create(p, server, port + 1);

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
