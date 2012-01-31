//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/SmtpAck.sj -d tests/classes/ 

package smtp.sj.messages;

abstract public class SmtpAck extends SmtpMessage
{
	abstract public String replyCode();	
	
	public String prefix() // Would be nice if we could generate patterns for the parseMessage routine using these constants.
	{
		return replyCode();
	}
}
