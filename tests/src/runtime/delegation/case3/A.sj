// bin/sessionjc tests/src/runtime/delegation/case3/A.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case3.A localhost 7777 localhost 8888

package runtime.delegation.case3;

import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class A // D-A-B-C => D-A-C-B (=> A-D-C-B)
{
	//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	//try { br.readLine(); } catch(IOException ioe) { }

	public static void main(String[] args) throws Exception 
	{
		final noalias protocol body { ?(String).!<String> } // The send makes this session a case 1 instead of case 2.
		final noalias protocol p_d { cbegin.@(body) }		
		final noalias protocol p_b { cbegin.!<String>.!<@(body)> } // The first send is unnecessary.

		final noalias SJService c_d = SJService.create(p_d, args[0], Integer.parseInt(args[1]));
		final noalias SJService c_b = SJService.create(p_b, args[2], Integer.parseInt(args[3]));
		
		noalias SJSocket s_d;			
		final noalias SJSocket s_b;			
		
		try (s_b, s_d)
		{
			s_d = c_d.request();
			s_b = c_b.request();		

			s_b.send("Hello from A!");
			s_b.pass(s_d);
		}
		finally
		{
		
		}
	}
}
