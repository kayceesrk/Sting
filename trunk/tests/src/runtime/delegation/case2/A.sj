// bin/sessionjc tests/src/runtime/delegation/case2/A.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case2.A localhost 8888

package runtime.delegation.case2;

import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class A 
{
	//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	//try { br.readLine(); } catch(IOException ioe) { }

	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_b { cbegin.!<String> }

		final noalias SJService c_b = SJService.create(p_b, args[0], Integer.parseInt(args[1]));
		
		final noalias SJSocket s_b;
		
		try (s_b)
		{
			s_b = c_b.request();
			
			s_b.send("Hello from A!");
		}
		finally
		{
		
		}
	}
}
