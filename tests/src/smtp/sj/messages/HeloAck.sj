//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/HeloAck.sj -d tests/classes/ 

package smtp.sj.messages;

public class HeloAck
{
	private String msg;
	
	public HeloAck(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
