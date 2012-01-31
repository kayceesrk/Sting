//$ bin/sessionjc tests/src/compiler/sessmeths/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.sessmeths.Test

package compiler.sessmeths;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{
	public static final noalias protocol p1 { !<String> }
	public static final noalias protocol p2 { cbegin.!<String>.!<int> }
	
	public static void main(String[] args) throws Exception
	{						
		final noalias SJService c1 = SJService.create(p2, "localhost", 8888);
		//final noalias SJService c2 = SJService.create(p2, "localhost", 9999);
		
		noalias SJSocket s1, s2;	

		try (s1)
		{
			s1 = c1.request();
			
			mymeth(s1);
			
			//s1.send(123);
		}
		finally
		{
			
		}
	}
	
	//private static void mymeth(final noalias @(p1) s2) throws SJIOException
	//private static void mymeth(final noalias @(p1).!<int> s2) throws SJIOException
	private static void mymeth(noalias @(p1).!<int> s2) throws SJIOException
	{	
		try (s2)
		{
			s2.send("");
			s2.send(123);
		}
		finally
		{
			
		}
	}
	
	/*class A
	{
		public void meth(final noalias !<Object> m)
		{
			
		}
	}
	
	class B extends A
	{
		public void meth(noalias !<String> m)
		{
			
		}
	}*/
}
