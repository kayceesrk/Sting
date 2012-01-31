//$ bin/sessionjc tests/src/Test2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ Test2

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test2  
{

	public void problem1() throws Exception
	{	
		final noalias protocol p1 { cbegin.!{L1:![!<int>]*} }

		final noalias SJService c1 = SJService.create(p1, "", 1234);

		noalias SJSocket s1;
		
		try (s1)
		{
			s1 = c1.request();

			s1.outbranch(L1)
			{
				s1.outwhile(true)
				{
					s1.send(123);
				}
			}
		}
		finally
		{

		}
	}
/*
	public void problem2() throws Exception
	{
		final noalias protocol p1 { cbegin.!<int> }
		
		final noalias SJService c1 = SJService.create(p1, "", 1234);
		
		noalias SJSocket s1;
		
		try (s1)
		{
			s1 = c1.request();
	
			s1.send(123);
		}
		finally
		{
			
		}
	}
*/	
}
