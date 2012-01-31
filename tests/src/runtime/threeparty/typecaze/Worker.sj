//$ bin/sessionjc tests/src/runtime/threeparty/typecaze/Worker.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.threeparty.typecaze.Worker false d d 9999

package runtime.threeparty.typecaze;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

public class Worker
{	
	private static protocol p_set { !<String>, !<int> }
	public static protocol p_worker sbegin.?(@(p_set))
	
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			//ss = SJServerSocket.create(p_server, port, createSJSessionParameters(setups, transports));
			ss = SJServerSocket.create(p_worker, port);
			
			while (true)
			{
				final noalias SJSocket s_d;
				final noalias SJSocket s_c;
				
				try (s_d, s_c)
				{
					s_d = ss.accept();
						
					System.out.println("w1: " + s_d.remainingSessionType());
					
					s_c = (@(p_set)) s_d.receive();
					
					System.out.println("w2: " + s_d.remainingSessionType());
					System.out.println("w3: " + s_c.remainingSessionType());
					
					typecase (s_c)
					{
            when (!<int>) 
            {
            	s_c.send(12345);
            }						
						when (!<String>) 
						{
            	System.out.println("w4: " + s_c.remainingSessionType());
            							
							s_c.send("ABCDE");
							            	
            	System.out.println("w5: " + s_c.remainingSessionType());							
						}
					}
				}
				catch (Exception x)
				{
					x.printStackTrace();
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
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String setups = args[1];
		String transports = args[2];

		int port = Integer.parseInt(args[3]);
		
		new Worker().run(debug, setups, transports, port);
	}
}
