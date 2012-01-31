//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/common/events/ChatEvent.sj -d tests/classes/

package oopsla.chat.common.events;

import java.io.Serializable;
import java.util.*;

abstract public class ChatEvent implements Serializable
{
	private Integer userId; // Could send the User?
	private Date stamp;
	
	/*public ChatEvent()
	{
		this.stamp = new Date();
	}*/
	
	public ChatEvent(Integer userId)
	{
		//this();
		this.stamp = new Date();
		this.userId = userId;
	}
	
	public Integer getUserId()
	{
		return userId;
	}

	public Date getTimeStamp()
	{
		return stamp;
	}	
}
