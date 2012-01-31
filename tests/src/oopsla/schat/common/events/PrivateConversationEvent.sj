//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/common/events/PrivateConversationEvent.sj -d tests/classes/

package oopsla.schat.common.events;

import java.util.*;

public class PrivateConversationEvent extends ChatEvent
{
	private Integer targetId;
	
	//private String requestorHostAddress;
	
	private Integer lock = new Integer(0);
	
	private Boolean[] accepted = new Boolean[] { null };
	
	private String hostAddress = null;
	private Integer port = null;
	
	public PrivateConversationEvent(Integer userId, Integer targetId/*, String requestorHostAddress*/)
	{
		super(userId);
		
		this.targetId = targetId;
		//this.requestorHostAddress = requestorHostAddress;
	}
		
	public Integer getRequestorId()
	{
		return getUserId();
	}
	
	public Integer getTargetId()
	{
		return targetId;
	}
		
	public void accept()
	{
		synchronized (lock)
		{
			accepted[0] = new Boolean(true);
			
			lock.notify();
		}
	}
	
	public void reject()
	{
		synchronized (lock)
		{
			accepted[0] = new Boolean(false);
			
			lock.notify();
		}
	}
	
	public boolean accepted()
	{
		synchronized (lock)
		{
			while (accepted[0] == null)
			{
				try
				{
					lock.wait();
				}
				catch (InterruptedException ie)
				{
					
				}
			}
			
			return accepted[0].booleanValue();
		}
	}
	
	public String getHostAddress()
	{
		synchronized (lock)
		{
			while (hostAddress == null)
			{
				try
				{
					lock.wait();
				}
				catch (InterruptedException ie)
				{
					
				}
			}
			
			return hostAddress;
		}
	}
	
	public void setHostAddress(String hostAddress)
	{
		synchronized (lock)
		{
			this.hostAddress = hostAddress;
			
			lock.notify();
		}
	}
	
	public int getPort()
	{
		synchronized (lock)
		{
			while (port == null)
			{
				try
				{
					lock.wait();
				}
				catch (InterruptedException ie)
				{
					
				}
			}
			
			return port.intValue();
		}
	}
	
	public void setPort(int port)
	{
		synchronized (lock)
		{
			this.port = new Integer(port);
			
			lock.notify();
		}
	}
	
	/*public String getRequestorHostAddress()
	{
		return requestorHostAddress;
	}*/
	
	public String toString()
	{
		return "<Private conversation event: " + getUserId() + ", " + getTargetId() + ">";
	}
}
