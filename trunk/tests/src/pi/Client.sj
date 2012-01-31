//$ bin/sessionjc tests/src/pi/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pi.Client 0.01 localhost 4441

package pi;

import java.io.*;
import java.math.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

/**
 * 
 * @author Andi, Raymond
 *
 */
public class Client
{	
	private final noalias protocol p_service_client { ^(Protocols.p_service) }
	
	private double target;
	private String serviceHost;
	private int servicePort;
	
	public Client(double target, String serviceHost, int servicePort)
	{
		this.target = target;
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
			
			service.send(new Double(target));
			
			BigDecimal pi = (BigDecimal) service.receive();
			
			System.out.println("pi = " + pi);			
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
		double target = Double.parseDouble(args[0]);
		String serviceHost = args[1];
		int servicePort = Integer.parseInt(args[2]);
		
		new Client(target, serviceHost, servicePort).run();
	}
}
