//$ bin/sessionjc tests/src/memtest/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ memtest.server.Server 8888

package memtest.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Server
{	
	final noalias protocol p_server { sbegin.?[?(String).!<String>]* }
	//final noalias protocol p_server { sbegin.?(String).!<int> }
	
	public Server(int port) throws Exception
	{
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocketImpl.create(p_server, 8888);						
			
			System.out.println("s1:");
			
			//while (true)
			{
				final noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
										
					System.out.println("s2:");
					
					s.inwhile()
					{
						/*s.inbranch()
						{
							case L1:
							{*/																
								//s.send((String) s.receive());
						
								noalias String m = (String) s.receive();
								
								s.send(m);
							/*}
						}*/
					}
				}
				catch (Exception x)
				{
					x.printStackTrace();
				}
			}
		}
		finally
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{						
		new Server(Integer.parseInt(args[0]));
	}
}
