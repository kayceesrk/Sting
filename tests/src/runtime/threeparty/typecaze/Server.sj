//$ bin/sessionjc tests/src/runtime/threeparty/typecaze/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.threeparty.typecaze.Server false d d 8888 localhost 9999

package runtime.threeparty.typecaze;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;

public class Server
{	
	public static protocol p_server1 sbegin.!<String>
	public static protocol p_server2 sbegin.!<int>
	
	public static protocol p_worker ^(Worker.p_worker)
	
	public void run(boolean debug, String setups, String transports, int port_server, String worker, int port_worker) throws Exception
	{
		startServer1(port_server, worker, port_worker);
		startServer2(port_server, worker, port_worker);
	}

	private void startServer1(final int port_server, final String worker, final int port_worker)
	{
		new Thread()
		{
			public void run()
			{
				final noalias SJServerSocket ss;							
				
				try (ss)
				{
					//ss = SJServerSocket.create(p_server1, port, createSJSessionParameters(setups, transports));
					ss = SJServerSocket.create(p_server1, port_server);
					
					while (true)
					{
						noalias SJSocket s_c;
						final noalias SJSocket s_s;
						
						try (s_c, s_s)
						{
							s_c = ss.accept();
			
							s_s = SJService.create(p_worker, worker, port_worker).request();
								
							s_s.send(s_c);
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
				catch (Exception x)
				{
					x.printStackTrace();
				}				
				finally
				{
					
				}
			}
		}.start();
	}

	private void startServer2(final int port_server, final String worker, final int port_worker)
	{
		new Thread()
		{
			public void run()
			{
				final noalias SJServerSocket ss;							
				
				try (ss)
				{
					//ss = SJServerSocket.create(p_server2, port, createSJSessionParameters(setups, transports));
					ss = SJServerSocket.create(p_server2, port_server + 10);
				
					while (true)
					{
						noalias SJSocket s_c;
						final noalias SJSocket s_s;
						
						try (s_c, s_s)
						{
							s_c = ss.accept();
			
							s_s = SJService.create(p_worker, worker, port_worker).request();
								
							s_s.send(s_c);
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
				catch (Exception x)
				{
					x.printStackTrace();
				}				
				finally
				{
					
				}
			}
		}.start();
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String setups = args[1];
		String transports = args[2];

		int port_server = Integer.parseInt(args[3]);
		
		String worker = args[4];
		int port_worker = Integer.parseInt(args[5]);
		
		new Server().run(debug, setups, transports, port_server, worker, port_worker);
	}
}
