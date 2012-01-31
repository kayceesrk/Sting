//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/MessageBodyAck.sj -d tests/classes/ 

package smtp.sj.messages;

public class MessageBodyAck
{
	private String msg;
	
	public MessageBodyAck(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
