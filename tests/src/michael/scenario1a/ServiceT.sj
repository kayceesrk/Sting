//$ bin/sessionjc tests/src/michael/scenario1a/ServiceT.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ michael.scenario1a.ServiceT 8888

package michael.scenario1a;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class ServiceT 
{
	public ServiceT(int port) throws SJIOException
	{

		final noalias protocol p_sa 
		{ 
			sbegin.
			?(String).
			?( 
				!<Double>.  
				?{
					ACCEPT: ?(Address).!<Date>,
					REJECT:
				} 
			) 
		}

		final noalias SJServerSocket ss_sa;
		
		try (ss_sa)
		{
			ss_sa = SJServerSocketImpl.create(p_sa, port); // Need assigns as well as declarations (for exception scope flexibility).

			while (true) // While not session type checked yet.
			{
				noalias SJSocket s_sa = null;		
				noalias SJSocket s_sc = null;

				try (s_sc, s_sa)
				{
					s_sa = ss_sa.accept();				
					//s_sc = (?(Address).!<Date>) s_sa.receive();
					String travDetails = (String) s_sa.receive();

					s_sc = (!<Double>.?{ACCEPT: ?(Address).!<Date>, REJECT: }) s_sa.receive();

					Double cost = getPrice();

					s_sc.send(cost);

					System.out.println("Requested journey: " + travDetails + "; cost: " + cost);	

					s_sc.inbranch() 
					{
						case ACCEPT: 
						{
							storeInDatabase((Address) s_sc.receive());

							Date date = getDate();

							System.out.println("Estimated dispatch date: " + date);

							s_sc.send(date);
						}
						case REJECT:
						{

						}
					}
				}
				catch (SJIOException ioe) 
				{ 
					ioe.printStackTrace(); 
				}
				catch (ClassNotFoundException cnfe) 
				{ 
					cnfe.printStackTrace(); 
				}
				catch (SJIncompatibleSessionException stise) 
				{ 
					stise.printStackTrace(); 
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

	private Double getPrice() 
	{
		return new Double((new Random().nextInt(20) - 10) + 100.00);
	}

	private Date getDate() 
	{
		return new Date();
	}

	public static void main(String[] args) throws SJIOException
	{
		new ServiceT(Integer.parseInt(args[0]));
	}
}
