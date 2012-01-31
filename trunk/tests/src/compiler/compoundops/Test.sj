//$ bin/sessionjc tests/src/compiler/compoundops/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.compoundops.Test

package compiler.compoundops;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{	
	public static void main(String[] args) throws Exception
	{						
		//final noalias protocol p1 { cbegin.?{L1:!<Integer>, L2:!<String>} }
		//final noalias protocol p2 { cbegin.!{L1:!<String>, L2:!<Integer>} }
		final noalias protocol p1 { cbegin.![!<int>]* }
		final noalias protocol p2 { cbegin.?[!<String>]* }
		//final noalias protocol p3 { cbegin.rec LAB[!<String>.#LAB] }
		//final noalias protocol p4 { cbegin.rec LAB[!<String>.#LAB] }
		
		final noalias SJSocket s1;
		final noalias SJSocket s2;
		final noalias SJSocket s3;
		final noalias SJSocket s4;
			
		try (s1, s2, s3, s4)
		{
			final noalias SJServerAddress c1 = SJServerAddress.create(p1, "localhost", 8888);
			final noalias SJServerAddress c2 = SJServerAddress.create(p2, "localhost", 9999);
			//final noalias SJServerAddress c3 = SJServerAddress.create(p3, "localhost", 7777);
			//final noalias SJServerAddress c4 = SJServerAddress.create(p4, "localhost", 6666);

			s1 = c1.request();
			s2 = c2.request();							
			//s3 = c3.request();
			//s4 = c4.request();
			
			s1.outwhile(s2.inwhile())
			{
				s1.send(123);
				s2.send("ABC");
			}
		}
		finally
		{
			
		}
	}
}
