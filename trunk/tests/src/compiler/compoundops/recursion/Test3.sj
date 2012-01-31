//$ bin/sessionjc tests/src/compiler/compoundops/recurzion/Test3.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.compoundops.recurzion.Test3

package compiler.compoundops.recurzion;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test3 
{	
	static protocol p_body rec X [!<int>.rec Y [!<String>.?{LX:#X, LY:#Y, LQ:}]]	
	
	public static void main(String[] args) throws Exception
	{						
		protocol p cbegin.@(p_body)
		                
		final noalias SJSocket s;
		
		try (s)
		{
			s = SJService.create(p, "localhost", 1234).request();
			
			mX(s);
		}
		finally
		{
			
		}		                        
	}
	
	private static void mX(final noalias @(p_body) s) throws SJIOException, ClassNotFoundException
	{
		s.recursion(X)
		{
			s.send(12345);
			
			mY(s);
		}
	}
	
	private static void mY(final noalias rec Y [!<String>.?{LX:@(p_body), LY:#Y, LQ:}] s) throws SJIOException, ClassNotFoundException
	{
		s.recursion(Y)
		{
			s.send("ABCDEF");
			
			s.inbranch() 
			{
				case LX:
				{
					//s.recurse(X);
					
					mX(s);
				}
				case LY:
				{
					//s.recurse(Y);
					
					mY(s);
				}
				case LQ:
				{
					
				}
			}
		}
	}	
}
