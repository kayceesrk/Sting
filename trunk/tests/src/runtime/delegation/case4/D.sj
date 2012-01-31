// bin/sessionjc tests/src/runtime/delegation/case4/D.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case4.D 7777

package runtime.delegation.case4;

import java.io.IOException;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class D 
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_c { !<String> }
		final noalias protocol p_a { sbegin.?(@(p_c)) }

		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocketImpl.create(p_a, Integer.parseInt(args[0]));
		
			final noalias SJSocket s_a;		
			final noalias SJSocket s_c;

			try (s_a, s_c)
			{
				s_a = ss.accept();
				
				s_c = (@(p_c)) s_a.receive();
				
				s_c.send("Hello from D!");
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
