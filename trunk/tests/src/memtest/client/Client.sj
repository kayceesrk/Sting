//$ bin/sessionjc tests/src/memtest/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ memtest.client.Client localhost 8888

package memtest.client;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

public class Client
{	
	final noalias protocol p_server { sbegin.?[?(String).!<String>]* }
	//final noalias protocol p_server { sbegin.?(String).!<int> }
	final noalias protocol p_client { ^(p_server) }
	
	public Client(String server, int port) throws Exception
	{
		final noalias String foo = 
			"ABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABC" +
			"ABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABC" +
			"ABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABC" +
			"ABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABCABC";		

		final noalias SJSocket s;	
		
		try (s)
		{
			s = SJService.create(p_client, server, port).request();					
			
			System.out.println("c1:");
			
			long start = System.nanoTime();
			
			int i = 0;
			
			s.outwhile(i++ < 10000)
			{			
				//s.outbranch(L1)
				{													
					//System.out.println("Received: " + s.receiveInt());

					String m1 = new Integer(i).toString() + foo; 
					
					s.send(m1);
					//s.passCopy(m1);
					
					String m2 = (String) s.receive();
				}
			}
			
			long finish = System.nanoTime();
			
			System.out.println("Time = " + (finish - start) / 1000000);
		}
		finally
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{						
		new Client(args[0], Integer.parseInt(args[1]));
	}
}
