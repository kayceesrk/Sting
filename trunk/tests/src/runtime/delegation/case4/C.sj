// bin/sessionjc tests/src/runtime/delegation/case4/C.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case4.C 8888

package runtime.delegation.case4;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class C 
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_d { ?(String).?(String) }
		final noalias protocol p_b { sbegin.?(@(p_d)) }

		final noalias SJServerSocket ss;

		try (ss)
		{	
			ss = SJServerSocketImpl.create(p_b, Integer.parseInt(args[0]));
		
			final noalias SJSocket s_b;
			final noalias SJSocket s_d;

			try (s_b, s_d)
			{
				s_b = ss.accept();

				s_d = (@(p_d)) s_b.receive();

				System.out.println("Received: " + (String) s_d.receive());
				System.out.println("Received: " + (String) s_d.receive());
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
