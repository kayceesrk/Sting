//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/chat/common/events/UserLeftEvent.sj -d tests/classes/

package oopsla.chat.common.events;

import java.util.*;

public class UserLeftEvent extends ChatEvent
{
	public UserLeftEvent(Integer userId)
	{
		super(userId);
	}
	
	public String toString()
	{
		return "<User left event: " + getUserId() + ">";
	}
}
