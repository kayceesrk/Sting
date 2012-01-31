//$ bin/sessionjc tests/src/compiler/sesstry/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.sesstry.Test

package compiler.sesstry;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{	
	public static void main(String[] args) throws Exception
	{						
		final noalias protocol p1 { cbegin.!<String>.?(String) }
		final noalias protocol p2 { cbegin.!<String>.?(String) }
		
		final noalias SJSocket s1;
		final noalias SJSocket s2;	
		final noalias SJSocket s4;
			
		try (s1, s2, s4)
		{
			final noalias SJServerAddress c1;
			
			c1 = SJServerAddress.create(p1, "localhost", 8888);
			
			s1 = c1.request();
			
			final noalias SJSocket s3;
			
			s4 = c1.request();
			
			<s4>.send("ABC");
			
			try (s3, s4)
			{
				s3 = c1.request();
				
				s4.receive();
				
				<s3>.send("ABC");
				s3.receive();
			}
			finally
			{
				
			}
			
			{						
				s2 = SJServerAddress.create(p2, "localhost", 8888).request();
											
				{
					noalias String x = "ABC";
					
					<s1, s2>.pass(x);
				}
				
				s2.receive();
			}
			
			s1.receive();			
		}
		finally
		{
			
		}
	}
}
