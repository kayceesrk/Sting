//$ bin/sessionjc -cp tests/classes/ tests/src/oopsla/schat/common/events/QuitEvent.sj -d tests/classes/

package oopsla.schat.common.events;

import java.util.*;

/**
 * Used as an optional dummy value, a bit like a FIN ACK. The correct operation of clients and servers should not depend on this event being sent ot received.
 */
public class QuitEvent extends ChatEvent
{	
	public QuitEvent(Integer userId)
	{
		super(userId);
	}
	
	public String toString()
	{
		return "<Quit event: " + getUserId() + ">";
	}
}
