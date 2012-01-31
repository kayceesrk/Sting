// bin/sessionjc tests/src/runtime/delegation/case3/C.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case3.C 9999

package runtime.delegation.case3;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class C 
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_d { ?(String).!<String> }		
		final noalias protocol p_a { ?(String).?(@(p_d)) }
		final noalias protocol p_b { sbegin.?(@(p_a)) }

		final noalias SJServerSocket ss;

		try (ss)
		{
			ss =  SJServerSocketImpl.create(p_b, Integer.parseInt(args[0]));
		
			final noalias SJSocket s_b;		
			final noalias SJSocket s_a;
			final noalias SJSocket s_d;

			try (s_b, s_a, s_d)
			{
				s_b = ss.accept();

				s_a = (@(p_a)) s_b.receive();

				System.out.println("Received: " + (String) s_a.receive());

				s_d = (@(p_d)) s_a.receive();

				System.out.println("Received: " + (String) s_d.receive());
				
				s_d.send("Hello from C!");				
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
