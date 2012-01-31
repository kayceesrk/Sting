//$ bin/sessionjc tests/src/selector/Test2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ selector.Test2

package selector;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test2 
{
	private static protocol p2 { ?(String) }
	private static protocol p1 { !<int>.@(p2) }
	//private static protocol p1 { !<int>.!<Boolean> }
	private static protocol p { sbegin.@(p1) }
	//private static protocol p { sbegin.!<Boolean> }
	private static protocol p_select { @(p1), @(p2) }
	//private static protocol p_select { @(p2) }
	//private static protocol p { sbegin.!<int>.?(String) }
	//private static protocol p_select { !<int>.?(String), ?(String) }

	public static void main(String[] args) throws Exception 
	{
		final noalias SJSelector selector = SJRuntime.selectorFor(p_select);
		
		try (selector) // FIXME.
		{
			noalias SJServerSocket ss;
			
			try (ss)			       
			{
				ss = SJServerSocket.create(p, 8888);
				
				selector.registerAccept(ss);

				while (true) 
				{
					noalias SJSocket s;
					
					try (s)
					{
						s = selector.select(SJSelector.ACCEPT | SJSelector.INPUT);
						
            typecase (s) 
            {
              when (@(p1))
            	//when (!<int>.?(String))
              {
              	s.send(123);
              	
              	//System.out.println("Received: " + (String) s.receive());
              	
              	selector.registerInput(s);
              	
              	//System.out.println("Received: " + (String) s.receive());
              }
              when (@(p2))
            	//when (?(String))
              {
              	System.out.println("Received: " + (String) s.receive());
            		//System.out.println("Received: " + (Integer) s.receive());
              }
            	/*when (!<Boolean>)
              {
            		s.send(new Boolean(true));
              }*/
            }
          }
					finally 
					{
						
					}
				}
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
