//$ bin/sessionjc tests/src/compiler/sessmeths/Test2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.sessmeths.Test2

package compiler.sessmeths;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test2
{
	public static final noalias protocol p1 { cbegin.?{L1: !{L3: !<String>}, L2: } }
	//public static final noalias protocol p1 { cbegin.!<String>.!<int> }
	public static final noalias protocol p2 { cbegin.!<!<String> > }
	
	public static void main(String[] args) throws Exception
	{						
		final noalias SJService c1 = SJService.create(p1, "localhost", 8888);
		final noalias SJService c2 = SJService.create(p2, "localhost", 9999);
		
		noalias SJSocket s1, s2;	

		try (s1)
		{
			s1 = c1.request();			
			
			s1.inbranch()		
			//if (true)
			{
				//s1.outbranch(L1)
				case L1:
				{
					try (s1, s2)
					{
						s2 = c2.request();
						
						s1.outbranch(L3)
						{			
							s2.pass(s1);
						}
					}
					finally
					{
						
					}
				}
			/*}
			else
			{*/
				//s1.outbranch(L2)
				case L2:
				{
					//s1.send("");
				}
			}
			
			//s1.send(123);
		}
		finally
		{
			
		}
	}
}
