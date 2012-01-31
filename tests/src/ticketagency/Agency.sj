//$ bin/sessionjc -cp tests/classes/ tests/src/ticketagency/Agency.sj -d tests/classes/
//$ (bin/sessionjc tests/src/ticketagency/Agency.sj -d tests/classes/)
//$ bin/sessionj -cp tests/classes/ ticketagency.Agency d d 9999 localhost 8888

package ticketagency;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.httpservlet.*;

class Agency 
{
	Agency(String setups, String transports, int port_a, String addr_s, int port_s) throws Exception 
	{
		final noalias protocol p_ac 
		{
		  sbegin.
		  ?[
		  	?(String).!<double>
		  ]*.
		  ?{
		    ACCEPT: ?(Address).!<Date>,
		  	REJECT:
		  }
		}

		final noalias protocol p_as { cbegin.!<?(Address).!<Date> > } // '>>' is a problem.

		final noalias SJServerSocket ss_ac;
		
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(setups, transports);
		
		try (ss_ac)
		{
			ss_ac = SJServerSocketImpl.create(p_ac, port_a, params);
			
			while (true) 
			{
				noalias SJSocket s_ac;
	
				try (s_ac)
				{
					s_ac = ss_ac.accept();
	
					s_ac.inwhile() 
					{
						String travDetails = (String) s_ac.receive();
	
						Double cost = (Double) getPrice(travDetails);
	
						System.out.println("Requested journey: " + travDetails + "; cost: " + cost);
	
						s_ac.send(cost.doubleValue());
					}
	
					s_ac.inbranch() 
					{
						case ACCEPT: 
						{
							final noalias SJService c_as = SJService.create(p_as, addr_s, port_s);
							final noalias SJSocket s_as;
	
							try (s_as)
							{
								s_as = c_as.request(params);
								
								s_as.pass(s_ac); // No more operations on s_ac allowed. 
							}		
							finally 
							{ 
							
							}
						}
						case REJECT: 
						{
						
						}
					}
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

	private Double getPrice(String travelMethod) 
	{
		return new Double((new Random().nextInt(20) - 10) + 100.00);
	}

	public static void main(String[] args) throws Exception 
	{
		String setups = args[0];
		String transports = args[1];
		
		int port_a = Integer.parseInt(args[2]);
		String host_s = args[3];
		int port_s = Integer.parseInt(args[4]);
		
		new Agency(setups, transports, port_a, host_s, port_s);
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
	
	private Address a = new Address("dummy"); // FIXME: currently a problem if Address is referred to in the delegation type but not actually used in the code.
}
