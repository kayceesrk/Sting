//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/common/events/UserLeftEvent.sj -d tests/classes/

package oopsla.schat.common.events;

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
