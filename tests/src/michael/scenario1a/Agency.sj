//$ bin/sessionjc -cp tests/classes/ tests/src/michael/scenario1a/Agency.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ michael.scenario1a.Agency 9999 localhost 8888

package michael.scenario1a;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

class Agency 
{
	Agency(int port_a, String addr_s, int port_s) throws SJIOException 
	{
		final noalias protocol p_ac 
		{
		  sbegin.
			?(String).!<Double>.
			?{
		    ACCEPT: ?(Address).!<Date>,
		  	REJECT:
			}
		}

		final noalias protocol p_as 
		{ 
			cbegin.
			!<String>.
			!<
				!<Double>.
				?{
		  	  ACCEPT: ?(Address).!<Date>,
		  		REJECT:
		  	} 
		  >
		}

		final noalias SJServerSocket ss_ac;
		
		try (ss_ac)
		{
			ss_ac = SJServerSocketImpl.create(p_ac, port_a);

			String addr_sa = "localhost";
			int port_sa = 7777;

			while (true) 
			{
				noalias SJSocket s_ac;

				try (s_ac)
				{
					s_ac = ss_ac.accept();

					String travDetails = (String) s_ac.receive();

					if(travDetails.startsWith("Train"))
					{
						final noalias SJService c_as = SJService.create(p_as, addr_s, port_s);
						final noalias SJSocket s_as;

						try (s_ac, s_as)
						{
							s_as = c_as.request();
						
							s_as.send(travDetails);
							s_as.send(s_ac); // No more operations on s_ac allowed. // Similarly if delegation other way round.
						}
						//catch (UnknownHostException uhe)
						finally
						{ 
							//uhe.printStackTrace(); 
						}
					}
					else
					{
						final noalias SJService c_asa = SJService.create(p_as, addr_sa, port_sa);
						final noalias SJSocket s_asa;
						
						try (s_ac, s_asa)
						{
							s_asa = c_asa.request();
							s_asa.send(travDetails);
							s_asa.send(s_ac); // No more operations on s_ac allowed. // Similarly if delegation other way round.
						}
						//catch (UnknownHostException uhe)
						finally
						{ 
							//uhe.printStackTrace(); 
						}				
					}
				}			
				catch (SJIOException ioe) 
				{ 
					ioe.printStackTrace();
				}
				catch (SJIncompatibleSessionException stise) 
				{ 
					stise.printStackTrace();
				}			
				catch (ClassNotFoundException cnfe) 
				{ 
					cnfe.printStackTrace();
				}				
			}
		}
		finally
		{
		
		}
	}

	public static void main(String[] args) throws SJIOException 
	{
		new Agency(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
	}
}
