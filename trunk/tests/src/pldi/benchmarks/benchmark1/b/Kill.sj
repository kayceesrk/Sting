//$ bin/sjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/b/Kill.sj -d tests/classes/
//$ bin/sj -cp tests/classes/ pldi.benchmarks.benchmark1.b.Kill localhost 8888

/**
 * 
 */
package pldi.benchmarks.benchmark1.b;

import java.io.*;
import java.net.*;

import sj.runtime.*;
import sj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

public class Kill 
{	
	private static final protocol p { begin }
	
	public static void main(String args[]) throws Exception 
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
					
		SJServerAddress c = SJServerAddress.create(p, server, port + 1);

		SJSocket s = SJFSocket.create(c);			
			
		try (s) 
		{
			s.request(); 	
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
