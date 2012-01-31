//$ bin/sessionjc -cp tests/classes/ tests/src/http/sj/client/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ http.sj.client.Client false www.google.com 80 /

package http.sj.client;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.net.SJSessionParameters.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;
import sessionj.runtime.session.*;

import http.sj.SJHttpFormatter;
import http.sj.messages.*;
//import http.sj.server.Server;

public class Client
{			
	private protocol p_client
	{
		//^(Server.p_server)
		cbegin
		.!<SJHttpGetRequest>
		.?(SJHttpReply)
	}
	
	public void run(boolean debug, String server, int port, String path) throws Exception
	{
		SJSessionParameters sparams = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, new SJHttpFormatter());
		
		final noalias SJSocket s;	
			
		try (s)
		{
			s = SJService.create(p_client, server, port).request(sparams);
			
			s.send(new SJHttpGetRequest(path));
			
			System.out.println("Received: \n\n" + (SJHttpReply) s.receive());
		}
		finally
		{
			
		}
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		 
		String server = args[1];
		int port = Integer.parseInt(args[2]);
		
		String path = args[3];
		
		new Client().run(debug, server, port, path);
	}
}
