//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/Rcpt5Ack.sj -d tests/classes/ 

package smtp.sj.messages;

public class Rcpt5Ack
{
	private String msg;
	
	public Rcpt5Ack(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
