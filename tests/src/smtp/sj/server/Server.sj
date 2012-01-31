//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ smtp.sj.server.Server false 8888 

package smtp.sj.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.net.SJSessionParameters.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;
import sessionj.runtime.transport.httpservlet.*;
import sessionj.runtime.session.*;

import smtp.sj.SJSmtpFormatter;
import smtp.sj.messages.*;

public class Server
{			
	public static final MyMessage LAB = new MyMessage("LAB");
	
	public protocol p_server
	{
		sbegin
		.!{
			$3: !<String>
		}
	}
	
	public void run(boolean debug, int port) throws Exception
	{
		SJSessionParameters sparams = SJTransportUtils.createSJSessionParameters(SJCompatibilityMode.CUSTOM, new SJSmtpFormatter());
		
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocket.create(p_server, port, sparams);

			final noalias SJSocket s;	
			
			try (s)
			{
				s = ss.accept();
				
				s.outbranch($3)
				//s.outbranch("LAB")
				{
					s.send("A");
				}
				
				//System.out.println("Received: " + (String) s.receive());
			}
			finally
			{
				
			}
		}
		finally
		{
			
		}		
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int port = Integer.parseInt(args[1]);
		
		new Server().run(debug, port);
	}
}
