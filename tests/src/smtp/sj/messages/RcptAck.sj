//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/RcptAck.sj -d tests/classes/ 

package smtp.sj.messages;

public class RcptAck
{
	private String msg;
	
	public RcptAck(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
