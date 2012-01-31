//$ bin/sessionjc tests/src/michael/scenario1a/Customer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ michael.scenario1a.Customer localhost 9999 100.00

package michael.scenario1a;

import java.net.UnknownHostException;
import java.util.Date;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class Customer 
{
	private static final noalias protocol p_ca 
	{
		cbegin.		
		!<String>.?(Double).
		!{
			ACCEPT: !<Address>.?(Date),
			REJECT:
		}
	}

	private String TRAVEL_METHOD = "Train Paris by Eurostar";
	//private String TRAVEL_METHOD = "Plane Paris by Eurostar";
	
	private Double MAX_PRICE;
	private Address ADDRESS = new Address("Customer's address.");

	Customer(String addr_a, int port_a, double maxPrice) throws SJIOException
	{
		MAX_PRICE = new Double(maxPrice);

		final noalias SJService c_ca = SJService.create(p_ca, addr_a, port_a);
		
		noalias SJSocket s_ca;
		int retry = 3;
		boolean decided = false;

		try (s_ca)
		{
			s_ca = c_ca.request();

			s_ca.send(TRAVEL_METHOD);

			Double cost = (Double) s_ca.receive();

			System.out.println("Received quote: " + cost);

			if (cost.compareTo(MAX_PRICE) < 0) 
			{
				decided = true;
			}
			
			if (decided) 
			{
				s_ca.outbranch(ACCEPT) 
				{					
					System.out.println("Quote accepted.");
					
					s_ca.send(ADDRESS);
					
					//System.out.println("Dispatch date: " + (Date)s_ca.receive());
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
		catch (SJIncompatibleSessionException ise) 
		{		
			ise.printStackTrace();
		}
		/*catch (UnknownHostException uhe) 
		{ 
			uhe.printStackTrace(); 
		}*/
		catch (ClassNotFoundException cnfe) { }
	}

	public static void main(String[] args) throws SJIOException
	{
		new Customer(args[0], Integer.parseInt(args[1]), Double.parseDouble(args[2]));
	}
}
