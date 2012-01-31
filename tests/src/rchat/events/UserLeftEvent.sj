// bin/sessionjc -cp tests/classes/ tests/src/rchat/events/UserLeftEvent.sj -d tests/classes/

package rchat.events;

public class UserLeftEvent extends RCEvent
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
