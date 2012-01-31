//$ bin/sessionjc tests/src/runtime/servicecopy/Bob.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.servicecopy.Bob 8888 localhost 9999

package runtime.servicecopy;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Bob 
{	
	public static final noalias protocol p_c { cbegin.?(String) }
		
	public static void main(String[] args) throws Exception
	{								
		int port_b = Integer.parseInt(args[0]);
		String host_c = args[1];
		int port_c = Integer.parseInt(args[2]);		

		final noalias protocol p_b { sbegin.!<@(p_c)> }
		
		final noalias SJServerSocket ss_b;
		
		try (ss_b)
		{
			ss_b = SJServerSocketImpl.create(p_b, port_b);
		
			final noalias SJService c_c = SJService.create(p_c, host_c, port_c);
			
			noalias SJSocket s_a;
			//noalias SJSocket s_a2;
			
			try (s_a)//, s_a2)
			{
				s_a = ss_b.accept();
				//s_a2 = ss_b.accept();
				
				s_a.copy(c_c);
				//<s_a, s_a2>.copy(c_c);
			}
			finally
			{
				
			}
		}
		finally
		{
			
		}
	}
}
