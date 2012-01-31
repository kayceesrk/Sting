//$ bin/sessionjc tests/src/typecaze/Test1.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ typecaze.Test1

package typecaze;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test1 
{
	//private static protocol p { ?(String).!<int>, !<Boolean> }
	private static protocol p { ?(String).!<int> }
	private static protocol p_service { cbegin.@(p) }
	//private static protocol p_service { cbegin.?(String).!<String> }

	public static void main(String[] args) throws Exception 
	{
		final noalias SJSelector selector = SJRuntime.selectorFor(p);
		
		final noalias SJService c = SJService.create(p_service, "localhost", 8888);
				
		try (selector)
		{
			noalias SJSocket s;
			
			try (s)			       
			{
				s = c.request();
				
				selector.registerInput(s);

				noalias SJSocket s1;
					
				try (s1)
				{
					s1 = selector.select(SJSelector.INPUT);
					
          typecase (s1) // FIXME: currently, 
          {
            when (@(p))
          	//when (?(String).!<int>)
            {
            	String m = (String) s1.receive();
            	
            	s1.send(123);
            }
            /*when (!<Boolean>)
            {
            	s1.send(new Boolean(true));
            }*/
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
		finally
		{
			
		}		
	}
}
