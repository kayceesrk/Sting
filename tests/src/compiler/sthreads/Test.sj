//$ bin/sessionjc tests/src/compiler/sthreads/Test.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ compiler.sthreads.Test

package compiler.sthreads;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Test 
{
	private static final noalias protocol q1 { !<String> }
	private static final noalias protocol q2 { !<int> }
	
	private static class MyThread extends SJThread
	{
		private int i;
		
		public MyThread(int i)
		{
			this.i = i;
		}
		
		/* public void spawn(final SJSocket s1, final SJSocket s2)
		 * {
		 * 	final MyThread _this = this;
		 * 
		 * 	new Thread() {
		 * 		public void run()
		 * 		{
		 * 			_this.run(s1, s2);
		 * 		}
		 * 	}.start();
		 * }
		 */
		
		public void srun(noalias @(q1) s1, noalias @(q2) s2, final noalias cbegin.@(q2) c3)
		{
			final noalias SJSocket s3;
			
			try (s1, s2, s3)
			{
				s3 = c3.request();
				
				s1.send("ABC");
				s2.send(i);
				s3.send(i);
			}
			catch (Exception x)
			{
				
			}
			finally
			{
				
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{						
		final noalias protocol p1 { cbegin.@(q1) }
		final noalias protocol p2 { cbegin.@(q2) }
		
		noalias SJSocket s1, s2;	

		try (s1, s2)
		{
			final noalias SJService c1 = SJService.create(p1, "localhost", 8888);
			final noalias SJService c2 = SJService.create(p2, "localhost", 99999);
			
			s1 = c1.request();
			s2 = c2.request();
			
			<s1, s2, c2>.spawn(new MyThread(123)); // new MyThread().spawn(s1, s2); // SJRuntime.spawn([s1, s2], new MyThread());
			
			/*<s1, s2>.spawn(
				new SJThread()
				{
					public void srun(noalias @(q1) s3, noalias @(q2) s4)
					{
						
					}
				}
			);*/
		}
		finally
		{
			
		}
	}
}
