//$ bin/sessionjc tests/src/compiler/compoundops/recurzion/Test2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.compoundops.recurzion.Test2

package compiler.compoundops.recurzion;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test2 
{	
	public static void main(String[] args) throws Exception
	{						
		protocol p cbegin.rec X [?(Integer).?(String).#X]
		                
		noalias SJSocket s;
		
		try (s)
		{
			s = SJService.create(p, "localhost", 1234).request();
			
			s.recursion(X)
			{
				//selector.register(s);
				
				Integer i = (Integer) s.receive();
				
				//m1(s);
				m2(s);
			}
		}
		finally
		{
			
		}		                        
	}
	
	private static void m1(noalias ?(String).rec X [?(Integer).?(String).#X] s) throws SJIOException, ClassNotFoundException
	{
		try (s)
		{
			m1(s);
		}
		finally
		{
			
		}
	}
	
	private static void m2(noalias ?(String).rec X [?(Integer).?(String).#X] s) throws SJIOException, ClassNotFoundException
	{
		try (s)
		{
			String m = (String) s.receive();
			
			s.recursion(X)
			{
				//selector.register(s);
				
				Integer i = (Integer) s.receive();
				
				//m1(s);
				
				m = (String) s.receive();
				
				s.recurse(X);
			}
		}
		finally
		{
			
		}
	}	
}
