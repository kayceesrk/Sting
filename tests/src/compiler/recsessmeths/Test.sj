//$ bin/sessionjc tests/src/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ Test

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{		
	static final noalias protocol p { rec X[!<String>.#X] }
	static final noalias protocol p1 { cbegin.@(p) }
	static final noalias protocol p2 { cbegin.!<@(p)> }
	
	void m1(final noalias @(p) s) throws SJIOException
	{
		s.recursion(X)
		{
			s.send("");
			s.recurse(X);
		}
	}
	
	public static void main(String[] args) throws Exception
	{			
		final noalias SJService c1 = SJService.create(p1, "localhost", 8888);
		final noalias SJService c2 = SJService.create(p2, "localhost", 8888);
		
		noalias SJSocket s1, s2;
		
		try (s1, s2)
		{
			s1 = c1.request();
			//s2 = c2.request();
						
			s1.recursion(X)
			{
				s1.send("");
				s1.recurse(X);
				
				//s2.send(s1);
			}
		}
		finally
		{
			
		}
	}
}
