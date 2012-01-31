//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/RcptTo.sj -d tests/classes/ 

package smtp.sj.messages;

public class RcptTo extends SmtpCommand
{
	public static final String RCPT_COMMAND = " TO:";
	
	private String recipient;
	
	public RcptTo(String recipient)
	{
		this.recipient = recipient;
	}
	
	public String command()
	{
		return RCPT_COMMAND;
	}
	
	public String body()
	{
		return "<" + recipient + ">";
	}
}
