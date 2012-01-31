// bin/sessionjc tests/src/runtime/delegation/case4/A.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case4.A localhost 7777 localhost 9999

package runtime.delegation.case4;

import java.io.IOException;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class A // D-A-B-C => A-D-C-B
{
	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_b { cbegin.!<String>.!<String> } // The first send is not really necessary.
		final noalias protocol p_d { cbegin.!<!<String> > }

		final noalias SJService c_d = SJService.create(p_d, args[0], Integer.parseInt(args[1]));
		final noalias SJService c_b = SJService.create(p_b, args[2], Integer.parseInt(args[3]));
		
		final noalias SJSocket s_d;		
		noalias SJSocket s_b;		
		
		try (s_d, s_b)
		{
			s_d = c_d.request();
			s_b = c_b.request();

			s_b.send("Hello from A!");

			s_d.pass(s_b);
		}
		finally
		{
			
		}
	}
}
