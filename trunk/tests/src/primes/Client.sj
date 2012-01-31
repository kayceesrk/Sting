//$ bin/sessionjc tests/src/primes/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ primes.Client 20 localhost 4441

package primes;

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
	
	private int range;
	private String serviceHost;
	private int servicePort;
	
	public Client(int range, String serviceHost, int servicePort)
	{
		this.range = range;
		this.serviceHost = serviceHost;
		this.servicePort = servicePort;
	}
	
	public void run() 
	{	
		final noalias SJService c_service = SJService.create(p_service_client, serviceHost, servicePort);
		
		final noalias SJSocket service;
		
		try (service)
		{					
			service = c_service.request();
			
			service.send(new Integer(range));
			
			Integer[] primes = (Integer[]) service.receive();
			
			System.out.print("primes = " + Arrays.toString(primes));			
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
		int range = Integer.parseInt(args[0]);
		String serviceHost = args[1];
		int servicePort = Integer.parseInt(args[2]);
		
		new Client(range, serviceHost, servicePort).run();
	}
}
