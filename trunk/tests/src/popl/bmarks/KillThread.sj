//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/KillThread.sj -d tests/classes/

/**
 *
 */
package popl.bmarks;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class KillThread extends Thread
{				
	private int port;
	private SJServerSocketCloser ssc;

	public KillThread(int port, SJServerSocketCloser ssc)
	{
		this.port = port;
		this.ssc = ssc;
	}

	public void run()
	{
		final noalias protocol q { sbegin }

		final noalias SJServerSocket ss;

		try (ss)
		{
			ss = SJServerSocketImpl.create(q, port);

			final noalias SJSocket s;

			try (s)
			{
				s = ss.accept();					
			}
			finally
			{

			}
		}
		catch (Exception x)
		{
			throw new RuntimeException(x);
		}
		finally
		{
			ssc.close();
		}
	}
}
