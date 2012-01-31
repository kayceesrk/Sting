// bin/sessionjc tests/src/runtime/delegation/case4/B.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case4.B localhost 8888 9999

package runtime.delegation.case4;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class B
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol body { ?(String).?(String) }
		final noalias protocol p_a { sbegin.@(body) }
		final noalias protocol p_c { cbegin.!<@(body)> }		
		
		final noalias SJServerSocket ss;

		try (ss)
		{
			ss = SJServerSocketImpl.create(p_a, Integer.parseInt(args[2]));
		
			final noalias SJService c_c = SJService.create(p_c, args[0], Integer.parseInt(args[1]));
			
			final noalias SJSocket s_c;	
			noalias SJSocket s_a;
	
			try (s_c, s_a)
			{
				s_c = c_c.request();
				
				s_a = ss.accept();
				
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
