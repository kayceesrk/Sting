// bin/sessionjc tests/src/runtime/delegation/case2/C.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case2.C 9999

package runtime.delegation.case2;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class C 
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_a { ?(String) }
		final noalias protocol p_b { sbegin.?(@(p_a)) }

		final noalias SJServerSocket ss;

		try (ss)
		{
			ss = SJServerSocketImpl.create(p_b, Integer.parseInt(args[0]));
			
			final noalias SJSocket s_b;
			final noalias SJSocket s_a;

			try (s_b, s_a)
			{
				s_b = ss.accept();

				s_a = (@(p_a)) s_b.receive();

				System.out.println("Received: " + s_a.receive());
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
