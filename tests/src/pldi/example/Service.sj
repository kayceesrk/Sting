//$ bin/sessionjc -cp tests/classes tests/src/pldi/example/Service.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.example.Service 4441 localhost 4442

package pldi.example;

import java.io.*;
import java.math.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 *
 * @author Raymond
 *
 */
public class Service
{
	private final noalias protocol p_worker_client { ^(Protocols.p_worker) }

	private int port;

	private int numWorkers;
	private List workerHosts;
	private List workerPorts;

	public Service(int port, List workerHosts, List workerPorts)
	{
		this.port = port;

		this.numWorkers = workerHosts.size();
		this.workerHosts = workerHosts;
		this.workerPorts = workerPorts;
	}

	public void run() throws Exception
	{
		final noalias SJServerSocket ss;

		try (ss)
		{
			ss = SJServerSocketImpl.create(Protocols.p_service, port);

			while (true)
			{
				final noalias SJSocket client;

				try (client)
				{
					client = ss.accept();

					noalias NoAliasLinkedList data = (NoAliasLinkedList) client.receive();

					final int interval = data.size() / numWorkers;
					final int[] res = new int[1];

					List threads = new LinkedList();

					for (int i = numWorkers - 1; i >= 0; i--)
					{
						final String host = (String) workerHosts.get(i);
						final int port = ((Integer) workerPorts.get(i)).intValue();

						int fi = i * interval;
						noalias NoAliasLinkedList segment;

						if (fi == 0)
						{
							segment = data;
						}
						else
						{
							segment = data.get(fi);
						}

						Thread t = new ServiceThread(host, port, segment, res);

						threads.add(t);

						t.start();
					}

					for (Iterator i = threads.iterator(); i.hasNext(); )
					{
						((Thread) i.next()).join();
					}

					client.send(new Integer(res[0]));
				}
				catch(SJIncompatibleSessionException ise)
				{
					System.err.println(serviceId() + " incompatible client: " + ise);
				}
				catch(SJIOException ioe)
				{
					System.err.println(serviceId() + " IO error: " + ioe);
				}
			}
		}
		finally
		{

		}
	}

	private class ServiceThread extends Thread
	{
		private String host;
		private int port;

		private noalias NoAliasLinkedList data;
		private int[] res;

		public ServiceThread(String host, int port, noalias NoAliasLinkedList data, int[] res)
		{
			this.host = host;
			this.port = port;
			this.data = data;
			this.res = res;
		}

		public void run()
		{
			final noalias SJService c_worker = SJService.create(p_worker_client, host, port);

			final noalias SJSocket worker;

			try (worker)
			{
				worker = c_worker.request();

				worker.send(data);

				int foo = ((Integer) worker.receive()).intValue();

				synchronized (res)
				{
					res[0] += foo;
				}

				System.out.println("res[0] = " + res[0]);
			}
			catch(SJIncompatibleSessionException ise)
			{
				System.err.println(serviceId() + " rejected by Worker: " + ise);
			}
			catch(SJIOException ioe)
			{
				System.err.println(serviceId() + " IO error: " + ioe);
			}
			catch (ClassNotFoundException cnfe)
			{
				throw new RuntimeException(cnfe);
			}
		}
	}

	public static void main(String args[]) throws Exception
	{
		int port = Integer.parseInt(args[0]);

		List workerHosts = new LinkedList();
		List workerPorts = new LinkedList();

		for (int i = 1; i < args.length; )
		{
			workerHosts.add(args[i++]);
			workerPorts.add(new Integer(Integer.parseInt(args[i++])));
		}

		new Service(port, workerHosts, workerPorts).run();
	}

	private String serviceId()
	{
		return "[Service " + port + "]";
	}
}
