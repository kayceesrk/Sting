// bin/sessionjc tests/src/runtime/delegation/case1a/B.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case1a.B 8888 localhost 9999

package runtime.delegation.case1a;

import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class B 
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol body { !<int> }
		final noalias protocol p_a { sbegin.@(body) }
		final noalias protocol p_c { cbegin.!<@(body)> }

		final noalias SJServerSocket ss;

		try (ss)
		{
			ss = SJServerSocketImpl.create(p_a, Integer.parseInt(args[0]));

			final noalias SJService c_c = SJService.create(p_c, args[1], Integer.parseInt(args[2]));
			
			noalias SJSocket s_a;
			final noalias SJSocket s_c;

			try (s_a, s_c)
			{
				s_a = ss.accept();
				
				s_c = c_c.request();		
				
				s_c.pass(s_a);
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
