//$ bin/sessionjc tests/src/compiler/compoundops/recurzion/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.compoundops.recurzion.Test

package compiler.compoundops.recurzion;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{	
	public static void main(String[] args) throws Exception
	{						
		protocol p cbegin.rec X [?(Integer).?(String).#X]
		                
		final noalias SJSocket s;
		
		try (s)
		{
			s = SJService.create(p, "localhost", 1234).request();
			
			s.recursion(X)
			{
				//selector.register(s);
				
				Integer m = (Integer) s.receive();
				
				m(s);
				
				s.recurse(X);
			}
		}
		finally
		{
			
		}		                        
	}
	
	private static void m(final noalias ?(String) s) throws SJIOException, ClassNotFoundException
	//private static void m(final noalias ?(String).?(String) s) throws SJIOException, ClassNotFoundException
	{
		String m1 = (String) s.receive();
		//String m2 = (String) s.receive();
	}
}
