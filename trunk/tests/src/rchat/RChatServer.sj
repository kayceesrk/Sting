//$ bin/sessionjc -cp tests/classes/ tests/src/rchat/RChatServer.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ rchat.RChatServer 8888

package rchat;

import java.io.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

//import rchat.events.*;

public class RChatServer
{
	public final noalias protocol p_sendmsgs_dual { ^(RChatClient.p_sendmsgs) }
	public final noalias protocol p_recmsgs_dual { ^(RChatClient.p_recmsgs) }	
	public final noalias protocol p_events_dual { ^(RChatClient.p_events) }
	public final noalias protocol p_control_dual { ^(RChatClient.p_control) } 

	public final noalias protocol p_sendmsgs_dual_body { ?[?(String)]* }
	public final noalias protocol p_recmsgs_dual_body { ?[!<String>]* } 
	
	private final EventLog events = new EventLog();
	private final MessageLog log = new MessageLog();	

	private int cid = 0;

	public RChatServer(int port) throws Exception
	{
		final noalias SJServerSocket ss_control;		
		
		try (ss_control)
		{
			ss_control = SJServerSocketImpl.create(p_control_dual, port);
					
			for ( ; true; cid++)
			{
				noalias SJSocket s_control;
				noalias SJSocket s_sendmsgs;	
				noalias SJSocket s_recmsgs;		
			
				try (s_control, s_recmsgs, s_sendmsgs)
				{
					s_control = ss_control.accept();
					
					s_control.send(cid);					
					
					String cname = (String) s_control.receive();
									
					final noalias SJService c_sendmsgs_dual = (cbegin.@(p_sendmsgs_dual_body)) s_control.receive();
					final noalias SJService c_recmsgs_dual = (cbegin.@(p_recmsgs_dual_body)) s_control.receive();					
					
					// Need a session type parallel construct.
					c_sendmsgs_dual.spawn(new SendMessagesDualThread(cid, cname));	
					c_recmsgs_dual.spawn(new ReceiveMessagesDualThread(cid)); 									
					
					s_control.spawn(new ControlThread(cid));				
					
					System.out.println("Client connected: " + cname + " (" + cid + ")");					
					
					events.addEvent(new UserJoinedEvent(cid, cname)); // events (shared state) needed for communication between indeterminate producers (each client) and consumers (all clients).
				}
				finally
				{
				
				}
			}
		}
		finally
		{

		}		
	}
	
	private class ControlThread extends SJThread
	{
		private int cid;
		
		private int i = 0;
		
		ControlThread(int cid)
		{
			this.cid = cid;
		}

		public void srun(noalias @(p_events_dual) s_control) 
		{
			try (s_control)
			{
				s_control.recursion(X)
				{
					s_control.inbranch()
					{
						case PING:
						{									
							RCEvent e = events.getEvent(i);
							
							s_control.outwhile(e != null)
							{
								if (e instanceof UserJoinedEvent)
								{
									s_control.outbranch(USER_JOINED)
									{
										UserJoinedEvent uje = (UserJoinedEvent) e;
									
										s_control.send(uje.getClientId());
										s_control.send(uje.getClientName());
									}
								}
								else 
								{
									if (!(e instanceof UserLeftEvent))
									{
										throw new RuntimeException("Unknown event type: " + e);
									}
								
									s_control.outbranch(USER_LEFT)
									{
										s_control.send(((UserLeftEvent) e).getClientId());
									}							
								}
													
								e = events.getEvent(++i);
							}
							
							s_control.recurse(X);
						}
						case BYE:
						{
							events.addEvent(new UserLeftEvent(cid));
							
							System.out.println("Client (" + cid + ") said: BYE");
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
	
	private class SendMessagesDualThread extends SJThread
	{	
		private int cid;
		private String cname;

		SendMessagesDualThread(int cid, String cname)
		{
			this.cid = cid;
			this.cname = cname;
		}

		public void srun(final noalias cbegin.@(p_sendmsgs_dual_body) c_recmsgs_dual) 
		{	
			final noalias SJSocket s_recmsgs_dual;
			
			try (s_recmsgs_dual)
			{								
				s_recmsgs_dual = c_recmsgs_dual.request();
		
				System.out.println("[SendMessagesDualThread] Connected to Client.");
				
				s_recmsgs_dual.inwhile()
				{
					String msg = (String) s_recmsgs_dual.receive();				
	
					System.out.println("(" + cid + ") received: " + msg);				
	
					log.putmsg(getTimeStamp() + " <" + cname + "> " + msg);				
				}
			}
			catch (Exception x)
			{
				
			}
		}
	}	
	
	private class ReceiveMessagesDualThread extends SJThread
	{
		private int cid;
		private String cname;
		private int i = 0;

		ReceiveMessagesDualThread(int cid)
		{
			this.cid = cid;
		}	

		public void srun(final noalias cbegin.@(p_recmsgs_dual_body) c_recmsgs_dual) 
		{
			final noalias SJSocket s_recmsgs_dual;
			
			try (s_recmsgs_dual)
			{				
				s_recmsgs_dual = c_recmsgs_dual.request();
			
				System.out.println("[ReceiveMessagesDualThread] Connected to Client.");
				
				s_recmsgs_dual.inwhile()
				{
					String msg = log.getmsg(i++);
	
					System.out.println("(" + cid + ") sending: " + msg);
	
					s_recmsgs_dual.send(msg);				
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
		new RChatServer(Integer.parseInt(args[0]));
	}

	private String getTimeStamp()
	{
		Calendar c = new GregorianCalendar();

		String year = "" + c.get(Calendar.YEAR);
		
		String month = null;

		switch (c.get(Calendar.MONTH))
		{
			case Calendar.JANUARY: month = "01"; break;
			case Calendar.FEBRUARY: month = "02"; break;
			case Calendar.MARCH: month = "03"; break;
			case Calendar.APRIL: month = "04"; break;
			case Calendar.MAY: month = "05"; break;
			case Calendar.JUNE: month = "06"; break;
			case Calendar.JULY: month = "07"; break;
			case Calendar.AUGUST: month = "08"; break;
			case Calendar.SEPTEMBER: month = "09"; break;
			case Calendar.OCTOBER: month = "10"; break;
			case Calendar.NOVEMBER: month = "11"; break;
			case Calendar.DECEMBER: month = "12"; break;
		}
		
		int d = c.get(Calendar.DAY_OF_MONTH);		
		String day = ((d < 10) ? "0" : "") + d;
		
		int h = c.get(Calendar.HOUR_OF_DAY);
		String hour = ((h < 10) ? "0" : "") + h;
		
		int m = c.get(Calendar.MINUTE);
		String minute = ((m < 10) ? "0" : "") + m;
		
		return "[" + year + month + day + "-" + hour + minute + "]";	
	}

	private class EventLog 
	{
		private LinkedList events = new LinkedList();
	
		public synchronized void addEvent(RCEvent e)
		{
			events.add(e);
		}	

		public synchronized RCEvent getEvent(int i)
		{
			if (i >= events.size())
			{			
				return null;
			}
			else 
			{
				return (RCEvent) events.get(i);				
			}
		}			
	}
}

abstract class RCEvent implements Serializable
{

}

class UserJoinedEvent extends RCEvent
{
	private int cid;
	private String cname;
	
	public UserJoinedEvent(int cid, String cname)
	{
		this.cid = cid;
		this.cname = cname;
	}
	
	public int getClientId()
	{
		return cid;
	}

	public String getClientName()
	{
		return cname;
	}
}

class UserLeftEvent extends RCEvent
{
	private int cid;
	
	public UserLeftEvent(int cid)
	{
		this.cid = cid;	
	}
	
	public int getClientId()
	{
		return cid;
	}
}
