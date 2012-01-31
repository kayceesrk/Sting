//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/client/net/ChatClientCommunicator.sj -d tests/classes/

package oopsla.chat.client.net;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;
import oopsla.chat.client.*;

public class ChatClientCommunicator
{	
	private ChatProtocols dummy; // Dummy reference, acting a bit like a static import...
	
	private ChatClient client;
	
	private boolean connected;
	
	private ClientOutThread cot;
	private ClientInThread cit;
	
	public ChatClientCommunicator(ChatClient client)
	{
		this.client = client;
		
		this.connected = false;
	}	
	
	public synchronized Integer connect(String host_s, int port_s, String userName) throws SJIncompatibleSessionException, SJIOException
	{
		if (connected)
		{
			System.err.println("[ChatClientCommunicator] Illegal connect request, already connected: " + host_s + ":" + port_s + ", " + userName);
		}
		
		final noalias SJService c_cs_main = SJService.create(ChatProtocols.p_cs, host_s, port_s);
		
		noalias SJSocket s_cs_main;
		
		try (s_cs_main)
		{
			s_cs_main = c_cs_main.request(); // FIXME: allow transports to be configured.
		
			ChatClient.debugPrintln("[ChatClientCommunicator] Connected to server at " + host_s + ":" + port_s);
			
			s_cs_main.send(userName);
			
			Integer userId = new Integer(s_cs_main.receiveInt());
			
			ChatClient.debugPrintln("[ChatClientCommunicator] Assigned user ID: " + userId);
			
			final noalias SJService c_cs_in = (cbegin.@(ChatProtocols.p_event_in_stream)) s_cs_main.receive();
			
			/*s_cs_main.outwhile(true) // Compilation hack.
			{
				s_cs_main.send(null);
			}*/
			
			ChatClient.debugPrintln("[ChatClientCommunicator] Starting Client in/out threads...");
			
			this.connected = true; // After this point, exceptions will not automatically cause the session to be closed. So need an external close mechanism.
			
			cot = (ClientOutThread) s_cs_main.spawn(new ClientOutThread(client, this));
			cit = (ClientInThread) c_cs_in.spawn(new ClientInThread(client, this));
			
			return userId;
		}
		catch (ClassNotFoundException cnfe)
		{
			this.connected = false;
			
			throw new SJIOException(cnfe);
		}
		finally
		{
			
		}
	}
	
	public void sendEvent(ChatEvent e)
	{
		cot.queueEvent(e);
	}
	
	public synchronized void disconnect()
	{
		//if (isConnected())
		{
			ChatClient.debugPrintln("[ChatClientCommunicator] Disconnecting...");
			
			this.connected = false;
			
			cot.disconnect();
			cit.disconnect();
		}
	}
	
	public boolean isConnected()
	{
		return connected;
	}	
}
