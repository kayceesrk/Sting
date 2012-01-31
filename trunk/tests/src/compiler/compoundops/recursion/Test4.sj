//$ bin/sessionjc tests/src/compiler/compoundops/recurzion/Test3.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.compoundops.recurzion.Test3

package compiler.compoundops.recurzion;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test3 
{	
	static protocol p_x rec X [!<int>.rec Y [!<String>.?{LX:#X, LY:#Y, LQ:}]]
	static protocol p_y rec Y [!<String>.?{LX:@(p_x), LY:#Y, LQ:}]
	
	public static void main(String[] args) throws Exception
	{						
		protocol p cbegin.@(p_x)
		                
		final noalias SJSocket s;
		
		try (s)
		{
			s = SJService.create(p, "localhost", 1234).request();
			
			s.recursion(X)
			{
				mX(s);
			}
		}
		finally
		{
			
		}		                        
	}
	
	private static void mX(final noalias !<int>.@(p_y) s) throws SJIOException, ClassNotFoundException
	{
		s.send(12345);
		
		s.recursion(Y)
		{
			mY(s);
		}
	}
	
	private static void mY(final noalias !<String>.?{LX:@(p_x), LY:@(p_y), LQ:} s) throws SJIOException, ClassNotFoundException
	{
		s.send("ABCDEF");
		
		s.inbranch() 
		{
			case LX:
			{							
				s.recursion(X)
				{
					mX(s);
				}	
			}
			case LY:
			{
				s.recursion(Y)
				{
					mY(s);
				}
			}
			case LQ:
			{
				
			}
		}
	}	
}
