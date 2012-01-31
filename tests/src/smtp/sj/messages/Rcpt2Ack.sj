//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/Rcpt2Ack.sj -d tests/classes/ 

package smtp.sj.messages;

public class Rcpt2Ack
{
	private String msg;
	
	public Rcpt2Ack(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
