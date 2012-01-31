//$ bin/sessionjc tests/src/ticketagency/Customer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ ticketagency.Customer d d localhost 9999 100.00

package ticketagency;

import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;

class Customer 
{
	private String TRAVEL_METHOD = "Paris by Eurostar";
	private Double MAX_PRICE;
	private Address ADDRESS = new Address("Customer's address.");	
	
	protocol p_ca 
	{
		cbegin
		.![
			!<String>
			.?(double)
		]*
		.!{
				ACCEPT: 
					!<Address>
					.?(Date)
			, REJECT:
		}
	}

	Customer(String setups, String transports, String addr_a, int port_a, double maxPrice) throws Exception 
	{
		boolean decided = false;
		int retry = 3;		
		
		SJSessionParameters params = SJTransportUtils.createSJSessionParameters(setups, transports);
		
		final noalias SJService c_ca = SJService.create(p_ca, addr_a, port_a);
		final noalias SJSocket s_ca;
		
		try (s_ca)
		{
			s_ca = c_ca.request(params);
			
			s_ca.outwhile(!decided && (retry-- > 0)) 
			{
				s_ca.send(TRAVEL_METHOD);
				
				//int cost = s_ca.receiveInt();
				double cost = s_ca.receiveDouble();
				
				System.out.println("Received quote: " + cost);
				
				decided = cost < maxPrice;
			}
			
			if (retry >= 0) 
			{
				s_ca.outbranch(ACCEPT) 
				{					
					System.out.println("Quote accepted.");
					
					s_ca.send(ADDRESS);
					
					//System.out.println("Dispatch date: " + (Date) s_ca.receive());
					System.out.println("Received dispatch date: " + s_ca.receive());
				}
			}
			else 
			{
				s_ca.outbranch(REJECT) 
				{			
					System.out.println("Quote rejected.");					
				}			
			}
		}
		finally
		{
			
		}
	}

	public static void main(String[] args) throws Exception
	{
		String setups = args[0];
		String transports = args[1];
		
		String host_a = args[2];
		int port_a = Integer.parseInt(args[3]);
		
		double maxPrice = Double.parseDouble(args[4]);
		
		new Customer(setups, transports, host_a, port_a, maxPrice);
	}	
}
