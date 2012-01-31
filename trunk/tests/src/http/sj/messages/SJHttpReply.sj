//$ bin/sessionjc -cp tests/classes/ tests/src/http/sj/messages/SJHttpReply.sj -d tests/classes/ 

package http.sj.messages;

public class SJHttpReply
{
	private String reply;
	
	public SJHttpReply(String reply)
	{
		this.reply = reply;
	}
	
	public String toString()
	{
		return reply;
	}
}
