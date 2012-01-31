//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/server/ServerOutThread.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.chat.server.ServerOutThread 8888 8898

package oopsla.chat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;

public class ServerOutThread extends Thread
{	
	private ChatProtocols dummy;
	
	private SJPort port_s_out;
	
	private ServerInThread sit;
	
	private List events = new LinkedList();
	
	private boolean run = true;
	
	public ServerOutThread(ServerInThread sit, SJPort port_s_out) 
	{ 
		this.sit = sit;
		
		this.port_s_out = port_s_out;
	}
	
	public void run()
	{
		System.err.println("[ServerOutThread] Server out thread started.");
		
		final noalias SJServerSocket ss_s;
		
		try (ss_s)
		{
			ss_s = SJServerSocket.create(ChatProtocols.p_s_out, port_s_out); // Although it's simpler conceptually to open a fresh server socket for each client, performance-wise, it's better to do this through a dedicated secondary server thread.

			ChatServer.debugPrintln("[ServerOutThread] Waiting for Client to connect...");
			
			noalias SJSocket s_sc_out;
			
			try (s_sc_out)
			{
				s_sc_out = ss_s.accept(); 
				
				ChatServer.debugPrintln("[ServerOutThread] Client connected.");
				
				ss_s.getCloser().close();
				
				s_sc_out.outwhile(run)
				{
					ChatEvent e = nextEvent();
					
					ChatServer.debugPrintln("[ServerOutThread] Sending event: " + e);
					
					s_sc_out.send(e);
				}
			}
			finally
			{
				
			}
		}
		catch (SJIncompatibleSessionException ise)
		{
			System.err.println("[ServerOutThread] Shouldn't get in here: " + ise);
		}
		catch (SJIOException ioe)
		{
			ChatServer.debugPrintln("[ServerOutThread] I/O error: " + ioe);
		}
		finally
		{
			sit.disconnect();
			
			ChatServer.debugPrintln("[ServerOutThread] Server out thread stopped.");
		}
	}
	 
	protected void disconnect()
	{
		ChatServer.debugPrintln("[ServerInThread] Told to disconnect...");
		
		if (run)
		{
			this.run = false;
			
			synchronized (events)
			{
				events.notify();
			}
		}
	}
	
	private ChatEvent nextEvent() throws SJIOException
	{
		synchronized (events)
		{
			while (events.isEmpty())
			{
				try
				{
					events.wait();
				}
				catch (InterruptedException ie) // Doesn't matter?
				{
					throw new SJIOException(ie);
				}
				
				if (!run)
				{
					ChatServer.debugPrintln("[ServerOutThread] Creating quit event...");
					
					//throw new SJIOException("[ClientOutThread] Disconnecting.");
					
					return new QuitEvent(ChatServer.CHAT_SERVER_ID);
				}
			}
			
			return (ChatEvent) events.remove(0);
		}
	}
	
	public void queueEvent(ChatEvent e)
	{
		synchronized (events)
		{
			events.add(e);
			events.notify();
		}
	}		
}
