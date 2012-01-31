// bin/sessionjc tests/src/runtime/delegation/case1a/A.sj -d tests/classes/
// bin/sessionj -cp tests/classes/ runtime.delegation.case1a.A localhost 8888

package runtime.delegation.case1a;

import java.io.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class A 
{
	//BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	//try { br.readLine(); } catch(IOException ioe) { }

	public static void main(String[] args) throws Exception 
	{
		final noalias protocol p_b { cbegin.?(int) } // No lost messages.

		final noalias SJService c_b = SJService.create(p_b, args[0], Integer.parseInt(args[1]));
		
		final noalias SJSocket s_b;
		
		try (s_b)
		{
			s_b = c_b.request();

			System.out.println("Received: " + s_b.receiveInt());
		}
		finally
		{
			
		}
	}
}
