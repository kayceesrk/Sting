//$ bin/sessionjc tests/src/ovex/Service.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ ovex.Service 4441 

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
public class Service
{	
	private final noalias protocol p_service { sbegin.?[?(Data).!<int>]* } 
	
	private int port;

	public Service(int port)
	{
		this.port = port;		
	}
	
	public void run() throws Exception 
	{			
		final noalias SJServerSocket ss;
		
		try (ss)
		{		
			ss = SJServerSocketImpl.create(p_service, port);

			while (true)
			{		
				final noalias SJSocket client;			
 				
				try (client)
				{			
					client = ss.accept();								

					int res = 0;
					
					client.inwhile()
					{
						noalias Data data = (Data) client.receive();
													
						res += data.getValue();
						
						client.send(res);
					}
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
	
	public static void main(String args[]) throws Exception 
	{
		int port = Integer.parseInt(args[0]);
		
		new Service(port).run();
	}
	
	private String serviceId()
	{
		return "[Service " + port + "]";
	}		
}
