//$ bin/sessionjc -cp tests/classes/ tests/src/runtime/servicecopy/Carol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.servicecopy.Carol 9999

package runtime.servicecopy;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Carol 
{	
	public static void main(String[] args) throws Exception
	{								
		int port_c = Integer.parseInt(args[0]);
		
		final noalias protocol p_c { ^(Bob.p_c) }
		
		final noalias SJServerSocket ss_c;
		
		try (ss_c)
		{
			ss_c = SJServerSocketImpl.create(p_c, port_c);

			noalias SJSocket s_a;
				
			try (s_a)
			{
				s_a = ss_c.accept();
				
				s_a.send("Hello from Carol!");
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
