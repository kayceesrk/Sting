//$ bin/sessionjc -cp tests/classes tests/src/pldi/example/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.example.Client localhost 4441 1 2 3 4 5

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
public class Client
{
	private final noalias protocol p_service_client { ^(Protocols.p_service) }

	private String serviceHost;
	private int servicePort;

	private noalias NoAliasLinkedList data;

	public Client(String serviceHost, int servicePort, noalias NoAliasLinkedList data)
	{
		this.serviceHost = serviceHost;
		this.servicePort = servicePort;
		this.data = data;
	}

	public void run()
	{
		final noalias SJService c_service = SJService.create(p_service_client, serviceHost, servicePort);

		final noalias SJSocket service;

		try (service)
		{
			service = c_service.request();

			service.send(data);

			Integer res = (Integer) service.receive();

			System.out.print("res = " + res);
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Client] rejected by service: " + ise);
		}
		catch (SJIOException ioe)
		{
			System.err.println("[Client] IO error: " + ioe);
		}
		catch (ClassNotFoundException cnfe)
		{
			throw new RuntimeException(cnfe);
		}
	}

	public static void main(String args[]) throws Exception
	{
		String serviceHost = args[0];
		int servicePort = Integer.parseInt(args[1]);

		noalias NoAliasLinkedList data = new NoAliasLinkedList(Integer.parseInt(args[2]));

		for (int i = 3; i < args.length; i++)
		{
			noalias NoAliasLinkedList tmp = new NoAliasLinkedList(Integer.parseInt(args[i]));
			tmp.setNext(data);
			data = tmp;
		}

		new Client(serviceHost, servicePort, data).run();
	}
}
