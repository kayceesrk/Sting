//$ bin/sessionjc tests/src/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ Test

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{	
	private static final noalias protocol q { cbegin }
	
	static class MyThread extends SJThread
	{
		//public void srun(final noalias cbegin c)
		public void srun(final noalias @(q) c)
		{
			
		}
	}
	
	//private static void m1(final noalias @(q) c)
	private static void m1(final noalias cbegin c)
	{

	}
	
	public static void main(String[] args) throws Exception
	{	
		final noalias protocol p { cbegin }
		
		final noalias SJService c = SJService.create(p, "localhost", 8888);
		
		noalias SJSocket s;
		
		m1(c);
		
		c.spawn(new MyThread());
		
		try (s)
		{
			s = c.request();		
		}
		finally
		{
			
		}		
	}
}
