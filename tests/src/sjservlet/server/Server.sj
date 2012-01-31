//$ bin/sessionjc tests/src/sjservlet/server/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ sjservlet.server.Server d false 8888

package sjservlet.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

public class Server
{	
	public final noalias protocol p_server { sbegin.?(String) }
	
	public void run(boolean debug, int port) throws Exception
	{
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocketImpl.create(p_server, port);						
			
			while (true)
			{
				final noalias SJSocket s;
				
				try (s)
				{
					s = ss.accept();
						
					System.out.println("Received: " + (String) s.receive());
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
		String transports = args[0];
		
		configureTransports(transports);

		boolean debug = Boolean.parseBoolean(args[1]); 
		int port = Integer.parseInt(args[2]);
		
		new Server().run(debug, port);
	}
	
	private static void configureTransports(String transports)
	{
		List ss = new LinkedList();
		List ts = new LinkedList();

		if (!transports.contains("d"))
		{
			if (transports.contains("f"))
			{
				ss.add(new SJFifoPair());
				ts.add(new SJFifoPair());
			}
			
			if (transports.contains("s"))
			{
				ss.add(new SJStreamTCP());
				ts.add(new SJStreamTCP());
			}
			
			if (transports.contains("m"))
			{
				ss.add(new SJManualTCP());			
				ts.add(new SJManualTCP());
			}
			
			SJTransportManager sjtm = SJRuntime.getTransportManager();
			
			sjtm.configureSetups(ss);
			sjtm.configureTransports(ts);							
		}		
	}		
}
