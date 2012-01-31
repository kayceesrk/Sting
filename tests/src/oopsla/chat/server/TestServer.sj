//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/server/TestServer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ oopsla.chat.server.TestServer 8888 8898

package oopsla.chat.server;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import oopsla.chat.common.*;
import oopsla.chat.common.events.*;

/**
 *  Only accepts a single connection at a time.
 */
public class TestServer
{	
	public static final noalias protocol p_cs_out { ![!<ChatEvent>]* }	
	public static final noalias protocol p_cs_in { ?[?(ChatEvent)]* }

	private static final noalias protocol p_s_out { cbegin.@(p_cs_in) } 
	
	private static final noalias protocol p_sc_main { sbegin.?(String).!<int>.!<@(p_s_out)>.^(p_cs_out) }	

	private int id = 100;
	
	//private List events = new LinkedList();
	
	public TestServer(int port_s, int port_s_out) throws Exception
	{ 
		final noalias SJServerSocket ss_s;
		
		try (ss_s)
		{
			ss_s = SJServerSocket.create(p_sc_main, port_s);
			
			final noalias SJSocket s_sc_main;
			
			try (s_sc_main)
			{
				s_sc_main = ss_s.accept(); 
				
				System.out.println("Username: " + (String) s_sc_main.receive());
				
				s_sc_main.send(id++);
				//s_sc_main.send(true);
				
				final noalias SJService c_s_out = SJService.create(p_s_out, s_sc_main.getLocalHostName(), port_s_out);			
				
				new TestOutThread(port_s_out).start();
				
				s_sc_main.copy(c_s_out);
				
				s_sc_main.inwhile()
				{
					System.out.println("Received event: " + (ChatEvent) s_sc_main.receive());
					//queueEvent((ChatEvent) s_sc_main.receive());
				}
			}
			finally
			{
				
			}
		}
		finally
		{
			
		}
	}	
	
	private class TestOutThread extends Thread
	{
		private final noalias protocol p_ss_out { sbegin.^(p_cs_in) }
		
		private int port_s_out;
		
		public TestOutThread(int port_s_out)
		{
			this.port_s_out = port_s_out;
		}
		
		public void run()
		{
			final noalias SJServerSocket ss_s_out;
			
			try (ss_s_out)
			{
				ss_s_out = SJServerSocket.create(p_ss_out, port_s_out);
				
				final noalias SJSocket s_sc_out;
				
				try (s_sc_out)
				{	
					s_sc_out = ss_s_out.accept();
					
					System.out.println("Client connected to Server out-port: " + port_s_out);
					
					//s_sc_out.outwhile(true)
					s_sc_out.outwhile(true)
					{
						//s_sc_out.send(nextEvent());
						
						try { Thread.sleep(1000); } catch (Exception x) { }
						
						s_sc_out.send(null);
					}
				}
				finally
				{
					
				}
			}
			catch (Exception x)
			{
				x.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception
	{
		int port_s = Integer.parseInt(args[0]);
		int port_s_out = Integer.parseInt(args[1]);
		
		new TestServer(port_s, port_s_out);
	}
	
	/*private ChatEvent nextEvent() throws SJIOException
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
	}*/	
}
