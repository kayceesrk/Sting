// bin/sessionjc -cp tests/classes/ tests/src/rchat/events/UserJoinedEvent.sj -d tests/classes/

package rchat.events;

public class UserJoinedEvent extends RCEvent
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
