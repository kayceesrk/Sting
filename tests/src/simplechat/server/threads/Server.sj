//$ bin/sessionjc -cp tests/classes/ tests/src/simplechat/server/threads/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ simplechat.server.threads.Server 8888 

package simplechat.server.threads;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import simplechat.*;
import simplechat.server.*;
import util.*;

public class Server   
{		
	private static final String transports = "d";

	private SJSessionParameters sparams;
	
	private final MessageLog log = new MessageLog();
	
	public Server()
	{
		this.sparams = TransportUtils.createSJSessionParameters(transports, transports);
	}
	
	private void run(int port) throws SJIOException, SJIncompatibleSessionException
	{
		final noalias SJServerSocket ss;
				
		try (ss)
		{
			ss = SJServerSocket.create(SimpleChatProtocols.p_serverToClient, port, sparams);						
			
			while (true)
			{
				noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
						
					s.inbranch()
					{
						case WRITE:
						{
							s.spawn(new ServerReceiveThread(log));
						}
						case READ:
						{
							s.spawn(new ServerSendThread(log));
						}
					}
				}
				catch (SJIOException ioe) 
				{
					ioe.printStackTrace();
				}
			}
		}
		finally
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		int port = Integer.parseInt(args[0]);
		
		Server serv = new Server();
		
		serv.run(port);
	}
}
