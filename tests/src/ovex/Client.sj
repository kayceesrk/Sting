//$ bin/sessionjc tests/src/ovex/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ ovex.Client localhost 4441 5 4 3 2 1 

package ovex;

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
	private final noalias protocol p_service_client { cbegin.![!<Data>.?(int)]* }
	
	private String serviceHost;
	private int servicePort;
	
	private noalias Data data;
	
	public Client(String serviceHost, int servicePort, noalias Data data)
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
			
			service.outwhile(data != null) 
			{
        noalias Data tmp = data.tail();
        
        service.send(data);
        
        System.out.println("Received: " + new Integer(service.receiveInt()).toString());
        
        data = tmp;
      }             
		}		
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[Client] rejected by service: " + ise);
		}
		catch (SJIOException ioe)
		{
			System.err.println("[Client] IO error: " + ioe);		
		}
		/*catch (ClassNotFoundException cnfe)
		{
			throw new RuntimeException(cnfe);
		}*/
	}
	
	public static void main(String args[]) throws Exception
	{	
		String serviceHost = args[0];
		int servicePort = Integer.parseInt(args[1]);		
		
		noalias Data data = new Data(Integer.parseInt(args[2]));
		
		for (int i = 3; i < args.length; i++)
		{
			noalias Data tmp = new Data(Integer.parseInt(args[i]));			
			tmp.setNext(data);			
			data = tmp;
		}

		new Client(serviceHost, servicePort, data).run();
	}
}
