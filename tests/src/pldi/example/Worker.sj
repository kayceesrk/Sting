//$ bin/sessionjc tests/src/pldi/example/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.example.Worker 4442

package pldi.example;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 *
 * @author Raymond
 *
 */
public class Worker
{
	private int port;

	public Worker(int port)
	{
		this.port = port;
	}

	public void run() throws Exception
	{
		final noalias SJServerSocket ss;

		try (ss)
		{
			ss = SJServerSocketImpl.create(Protocols.p_worker, port);

			while (true)
			{
				final noalias SJSocket service;

				try (service)
				{
					service = ss.accept();

					noalias NoAliasLinkedList data = (NoAliasLinkedList) service.receive();

					System.out.println(workerId() + " 1: " + data.toString());

					int res = 0;

					for ( ; data != null; data = data.getNext())
					{
						res += data.getValue();
					}

					System.out.println(workerId() + " 2: " + res);

					service.send(new Integer(res));
				}
				catch (SJIncompatibleSessionException ise)
				{
					System.err.println(workerId() + " incompatible service: " + ise);
				}
				catch (SJIOException ioe)
				{
					System.err.println(workerId() + " IO error: " + ioe);
				}
			}
		}
		finally
		{

		}
	}

	public static void main(String[] args) throws Exception
	{
		int port = Integer.parseInt(args[0]);

		new Worker(port).run();
	}

	private String workerId()
	{
		return "[Worker " + port + "]";
	}
}
