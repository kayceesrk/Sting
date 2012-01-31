//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/appserver/frontend/Portal.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.appserver.frontend.Portal false h h 8888 localhost 9911 localhost 9922 localhost 9933

package oopsla.appserver.frontend;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import util.*;

import oopsla.appserver.server.Server;

public class Portal
{	
	public static final noalias protocol p_sc { @(Server.p_sc) } // Should be generalised to support multiple servers of different types.
	
	public static final noalias protocol p_p 
	{ 
		sbegin.
		?(String).
		!<String>.
		?{
			SERVER1: @(p_sc),
			SERVER2: @(p_sc),
			SERVER3: @(p_sc)
		}
	}
	
	public static final noalias protocol p_ps { cbegin.!<@(p_sc)> } 
	
	public Portal()
	{
		
	}
	
	public void run(boolean debug, String setups, String transports, int port_p, String host_s1, int port_s1, String host_s2, int port_s2, String host_s3, int port_s3) throws Exception
	{
		SJSessionParameters params = TransportUtils.createSJSessionParameters(setups, transports);
		
		final noalias SJServerSocket ss_p;
		
		final noalias SJService c_ps1 = SJService.create(p_ps, host_s1, port_s1);
		final noalias SJService c_ps2 = SJService.create(p_ps, host_s2, port_s2);
		final noalias SJService c_ps3 = SJService.create(p_ps, host_s3, port_s3);
		
		try (ss_p)
		{
			ss_p = SJServerSocket.create(p_p, port_p, params);						
			
			while (true)
			{
				noalias SJSocket s_pc;
				
				try (s_pc)
				{
					s_pc = ss_p.accept(); // Not bothering to support concurrent clients yet.
						
					System.out.println("Accepted connection from: " + s_pc.getHostName() + ":" + s_pc.getPort()); 
					
					System.out.println("Received: " + (String) s_pc.receive());				
					
					s_pc.send("Hello from Portal!");		
										
					s_pc.inbranch()
					{
						case SERVER1:
						{
							final noalias SJSocket s_ps1;
							
							try (s_ps1)
							{
								s_ps1 = c_ps1.request(params);
								
								s_ps1.send(s_pc);
							}
							finally
							{
								
							}
						}
						case SERVER2:
						{
							final noalias SJSocket s_ps2;
							
							try (s_ps2)
							{
								s_ps2 = c_ps2.request(params);
								
								s_ps2.send(s_pc);
							}
							finally
							{
								
							}
						}
						case SERVER3:
						{
							final noalias SJSocket s_ps3;
							
							try (s_ps3)
							{
								s_ps3 = c_ps3.request(params);
								
								s_ps3.send(s_pc);
							}
							finally
							{
								
							}
						}				
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
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String setups = args[1];
		String transports = args[2];
 
		int port_p = Integer.parseInt(args[3]);
		
		String host_s1 = args[4];
		int port_s1 = Integer.parseInt(args[5]);
		
		String host_s2 = args[6];
		int port_s2 = Integer.parseInt(args[7]);
		
		String host_s3 = args[8];
		int port_s3 = Integer.parseInt(args[9]);
		
		new Portal().run(debug, setups, transports, port_p, host_s1, port_s1, host_s2, port_s2, host_s3, port_s3);
	}	
}
