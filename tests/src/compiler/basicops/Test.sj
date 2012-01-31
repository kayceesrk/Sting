//$ bin/sessionjc tests/src/compiler/basicops/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.basicops.Test

package compiler.basicops;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{	
	public static void main(String[] args) throws Exception
	{						
		final noalias protocol p1 { cbegin.!<String>.?(?(String)) }
		//final noalias protocol p2 { cbegin.!(?(String)).?(String) }
		
		final noalias SJSocket s1;
		final noalias SJSocket s2;
			
		try (s1, s2)
		{
			final noalias SJServerAddress c1 = SJServerAddress.create(p1, "localhost", 8888);
			//final noalias SJServerAddress c2 = SJServerAddress.create(p2, "localhost", 8888);						

			s1 = c1.request();
			
			if (true)
			{				
				s1.send("ABC");
				
				/*s2 = c2.request();
				s2.passCopy(s1);
				s2.receive();*/
			}
			else
			{
				s1.send("ABC");
			}
			
			//s1.receive();
			
			final noalias protocol q { ?(String) }
			
			s2 = (@(q)) s1.receive();			
			//s2 = (?(String)) s1.receive();
			s2.receive();
		}
		finally
		{
			
		}
	}
}
