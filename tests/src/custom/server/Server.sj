//$ bin/sessionjc -cp tests/classes/ tests/src/custom/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ custom.server.Server false d d 8888

package custom.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.session.*;
import sessionj.runtime.transport.*;

import custom.*;

public class Server
{	
	public protocol p_body rec X [?{L1: !<String>.#X, L2: !<String>}]
	public protocol p_server sbegin.@(p_body)
	
	public void run(boolean debug, String setups, String transports, int port) throws Exception
	{
		//SJSessionParameters params = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, new MyFormatter());
		SJSessionParameters params = new SJSessionParameters(SJCompatibilityMode.CUSTOM, MyFormatter.class);
		
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocket.create(p_server, port, params);
			
			//while (true)
			{
				final noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
						
					System.out.println(s.currentSessionType());
					System.out.println(s.remainingSessionType() + "\n");
					
					m(s);
				}
				/*catch (Exception x)
				{
					x.printStackTrace();
				}*/
				finally 
				{
					
				}
			}
		}
		finally
		{
			
		}
	}
	
	//public protocol p_unfold rec X [?{L1: !<String>.#X, L2: !<String>}] 
	
	private int i = 0;
	
	private void m(final noalias @(p_body) s) throws SJIOException, ClassNotFoundException
	{
		s.recursion(X)
		{
			s.inbranch()
			{
				case L1:
				{
					System.out.println(s.currentSessionType());
					System.out.println(s.remainingSessionType() + "\n");
				
					s.send(new Integer(i++).toString() + "\n");
				
					System.out.println(s.currentSessionType());
					System.out.println(s.remainingSessionType() + "\n");
				
					//Thread.sleep(1000);
					
					//s.recurse(X);					
					m(s);
				}
				case L2:
				{
					System.out.println(s.currentSessionType());
					System.out.println(s.remainingSessionType() + "\n");
					
					s.send("BYE\n");
					
					System.out.println(s.currentSessionType());
					System.out.println(s.remainingSessionType() + "\n");
				}
			}
		}		
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String setups = args[1];
		String transports = args[2];
		
		int port = Integer.parseInt(args[3]);
		
		new Server().run(debug, setups, transports, port);
	}
}
