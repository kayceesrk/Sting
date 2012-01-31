// bin/sessionjc tests/src/runtime/delegation/case3/D.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case3.D 7777

package runtime.delegation.case3;

import java.io.IOException;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class D 
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_a { sbegin.!<String>.?(String) }

		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocketImpl.create(p_a, Integer.parseInt(args[0]));
		
			final noalias SJSocket s_a;		

			try (s_a)
			{
				s_a = ss.accept();			
				
				s_a.send("Hello from D!");
				
				System.out.println("Received: " + (String) s_a.receive());
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
