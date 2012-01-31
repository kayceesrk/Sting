//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.Kill localhost 8888

/**
 * 
 */
package benchmarks;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

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
