//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/common/events/MessageEvent.sj -d tests/classes/

package oopsla.schat.common.events;

import java.util.*;

public class MessageEvent extends ChatEvent
{
	private Integer targetId;

	private String msg; 
	
	public MessageEvent(Integer userId, Integer targetId, String msg)
	{
		super(userId);
		
		this.targetId = targetId;
		this.msg = msg;
	}
		
	public Integer getSenderId()
	{
		return getUserId();
	}
	
	public Integer getTargetId()
	{
		return targetId;
	}
	
	public String getMessage()
	{
		return msg;
	}
	
	public String toString()
	{
		return "<Message event: " + getUserId() + ", " + getTargetId() + ", " + msg + ">";
	}
}
