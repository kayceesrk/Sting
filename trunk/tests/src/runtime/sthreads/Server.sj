//$ bin/sessionjc tests/src/runtime/sthreads/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.sthreads.Server 8888

package runtime.sthreads;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Server 
{	
	public static final noalias protocol p { ![!<int>]* }
	
	static class ServerThread extends SJThread
	{
		public void srun(noalias @(p) s)
		{
			int i = 0;
			
			try (s)
			{
				s.outwhile(true)
				{
					s.send(i++);
					
					Thread.sleep(1000);
				}
			}
			catch (Exception x)
			{
				
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{								
		final noalias protocol p_server { sbegin.@(p) }
		
		int port = Integer.parseInt(args[0]);
		
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocketImpl.create(p_server, port);

			while (true)
			{
				noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
					
					<s>.spawn(new ServerThread());
				}
				finally
				{
					
				}
			}
		}
		finally
		{
			
		}
	}
}
