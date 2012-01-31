//$ bin/sjc -cp tests/classes/ tests/src/benchmarks/benchmark1/b/Kill.sj -d tests/classes/
//$ bin/sj -cp tests/classes/ benchmarks.benchmark1.b.Kill localhost 8888

/**
 * 
 */
package benchmarks.benchmark1.b;

import java.io.*;
import java.net.*;

import sj.runtime.*;
import sj.runtime.net.*;

import benchmarks.BinaryTree;

public class Kill 
{	
	public static final int KILL_PORT_ADJUST = 2;

	private static final protocol p { begin }
	
	public static void main(String args[]) throws Exception 
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
					
		SJServerAddress c = SJServerAddress.create(p, server, port + KILL_PORT_ADJUST);

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
