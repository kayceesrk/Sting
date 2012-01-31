//$ bin/sessionjc -cp tests/classes/ tests/src/runtime/sthreads/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.sthreads.Test localhost 8888

package runtime.sthreads;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Client
{	
	public static void main(String[] args) throws Exception
	{			
		String host = args[0];
		int port = Integer.parseInt(args[1]); 
		
		final noalias protocol p_client { cbegin.^(Server.p) }

		final noalias SJSocket s;
		
		try (s)
		{
			s = (SJService.create(p_client, host, port)).request();
						
			s.inwhile()
			{
				System.out.println("Received: " + s.receiveInt());
			}
		}
		finally
		{
			
		}
	}
}
