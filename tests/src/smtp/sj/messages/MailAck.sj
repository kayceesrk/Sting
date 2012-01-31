//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/MailAck.sj -d tests/classes/ 

package smtp.sj.messages;

public class MailAck
{
	private String msg;
	
	public MailAck(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
