//$ bin/sessionjc -cp tests/classes/ tests/src/runtime/servicecopy/Alice.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.servicecopy.Alice localhost 8888

package runtime.servicecopy;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Alice
{	
	public static void main(String[] args) throws Exception
	{			
		String host_b = args[0];
		int port_b = Integer.parseInt(args[1]); 
		
		final noalias protocol p_b { cbegin.?(@(Bob.p_c)) }
		final noalias SJService c_b = SJService.create(p_b, host_b, port_b);
		
		final noalias SJSocket s_b, s_c;
		
		try (s_b, s_c)
		{
			s_b = c_b.request();
					
			final noalias SJService c_c = (@(Bob.p_c)) s_b.receive();
			
			s_c = c_c.request(); 
			
			System.out.println("Received: " + (String) s_c.receive());
		}
		finally
		{
			
		}
	}
}
