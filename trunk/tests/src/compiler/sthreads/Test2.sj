//$ bin/sessionjc tests/src/compiler/sthreads/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.sthreads.Test

package compiler.sthreads;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{
	private static final noalias protocol q1 { !<String>.?(String) }
	private static final noalias protocol q2 { !<String>.?(String) }
	
	private static class MyThread1 extends SJThread
	{
		public void run(@(q1) s1, @(q2) s2)
		{
			try (s1, s2)
			{
				
			}
			finally
			{
				
			}
		}
	}
	
	private static class MyThread2 extends SJThread
	{
		public void run(@(q1) s1, @(q2) s2)
		{
			
		}
	}
	
	private static class MyUncaughtExceptionHandler extends SJUncaughtExceptionHandler
	{
		public void uncaughtException(SJThread t, Throwable x)
		{
			
		}
	}	
	
	public static void main(String[] args) throws Exception
	{						
		final noalias protocol p1 { cbegin.@(q1) }
		final noalias protocol p2 { cbegin.@(q2) }
		
		final noalias SJSocket s1;
		final noalias SJSocket s2;	

		try (s1, s2)
		{
			final noalias SJServerAddress c1 = SJServerAddress.create(p1, "localhost", 8888);
			final noalias SJServerAddress c2 = SJServerAddress.create(p2, "localhost", 8889);
			
			s1 = c1.request();
			s2 = c2.request();
			
			<s1, s2>.spawn(new MyThread1());
			<s1, s2>.spawn(new MyThread2(), new MyUncaughtExceptionHandler());
		}
		finally
		{
			
		}
	}
}
