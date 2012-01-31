//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/d/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.d.Kill localhost 8888

/**
 * 
 */
package pldi.benchmarks.benchmark1.d;

import java.io.*;
import java.net.*;

public class Kill 
{	
	public static void main(String args[]) throws Exception 
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
					
		Socket s;			
			
		try  
		{
			s = new Socket(server, port + 1); 	
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
