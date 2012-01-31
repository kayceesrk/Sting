//$ bin/sessionjc tests/src/ticketagency/Service.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ ticketagency.Service d d 8888

package ticketagency;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.httpservlet.*;

class Service 
{
	public Service(String setups, String transports, int port) throws Exception
	{
		final noalias protocol p_sc { ?(Address).!<Date> }

		//final noalias protocol p_sa { sbegin.?(?(Address).!<Date>) }
		final noalias protocol p_sa { sbegin.?(@(p_sc)) }

		final noalias SJServerSocket ss_sa;
		
		try (ss_sa)
		{
			ss_sa = SJServerSocketImpl.create(p_sa, port, SJTransportUtils.createSJSessionParameters(setups, transports)); 		
		
			while (true) 
			{
				final noalias SJSocket s_sa;		
				final noalias SJSocket s_sc;
				
				try (s_sc, s_sa)
				{
					s_sa = ss_sa.accept();				
					
					// Any one is OK.
					//s_sc = (?(Address).!<Date>) s_sa.receive(); 
					//s_sc = (@(p_sc)) s_sa.receive();
					s_sc = (@(p_sc)) s_sa.receive(s_sa.getParameters());
	
					storeInDatabase((Address) s_sc.receive());
					
					Date date = getDate();
					
					System.out.println("Estimated dispatch date: " + date);
					
					s_sc.send(date);
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

	private void storeInDatabase(Address custAddr) 
	{
		System.out.println("Storing customer address: " + custAddr);
	}

	private Date getDate() 
	{
		return new Date();
	}

	public static void main(String[] args) throws Exception
	{
		String setups = args[0];
		String transports = args[1];
		
		int port_s = Integer.parseInt(args[2]);
		
		new Service(setups, transports, port_s);
	}
	
	/*private static SJSessionParameters createSJSessionParameters(String setups, String transports) throws SJSessionParametersException
	{		
		SJSessionParameters params;
		
		if (setups.contains("d") && transports.contains("d"))
		{
			params = new SJSessionParameters();
		}
		else
		{
			List ss = new LinkedList();
			List ts = new LinkedList();				
			
			parseTransportFlags(ss, setups);
			parseTransportFlags(ts, transports);
								
			params = new SJSessionParameters(ss, ts);
		}

		return params;
	}
	
	private static void parseTransportFlags(List ts, String transports)
	{
		if (transports.contains("d"))
		{
			//ts.add(new SJFifoPair());
			ts.add(new SJStreamTCP());
			
			return;
		}
		
		char[] cs = transports.toCharArray();
		
		for (int i = 0; i < cs.length; i++)
		{
			switch (cs[i])
			{
				/*case 'f':
				{
					ts.add(new SJFifoPair());
					
					break;
				}*
				case 's':
				{
					ts.add(new SJStreamTCP());
					
					break;
				}					
				case 'm':
				{			
					ts.add(new SJManualTCP());
					
					break;
				}					
				case 'h':
				{			
					ts.add(new SJHTTPServlet());
					
					break;
				}
			}
		}					
	}*/		
}
