//$ bin/sessionjc -cp tests/classes/ tests/src/simplechat/client/ClientWriter.sj -d tests/classes/ 
//$ bin/sessionj -cp tests/classes/ simplechat.client.ClientWriter localhost 8888 ray

package simplechat.client;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import simplechat.*;
import util.*;

public class ClientWriter   
{		
	private static final String transports = "d";

	private SJSessionParameters sparams;
	
	public ClientWriter()
	{	
		this.sparams = TransportUtils.createSJSessionParameters(transports, transports);
	}
	
	private void run(String server, int port, String uid) throws SJIOException, SJIncompatibleSessionException
	{
		final noalias SJService c = SJService.create(SimpleChatProtocols.p_clientToServer, server, port);
		
		noalias SJSocket s;
		
		try (s)
		{
			s = c.request(sparams);

			s.outbranch(WRITE)
			{
				Scanner scanner = new Scanner(System.in);
				
				s.outwhile(true)
				{
					String msg = scanner.nextLine();
					
					s.send("<" + uid + "> " + msg);
				}
			}
		}
		finally
		{
			
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		String server = args[0];
		int port = Integer.parseInt(args[1]);
		String uid = args[2];
		
		ClientWriter cw = new ClientWriter();
		
		cw.run(server, port, uid);
	}
}
