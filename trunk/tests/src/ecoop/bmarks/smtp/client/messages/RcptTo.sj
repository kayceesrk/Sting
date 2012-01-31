package ecoop.bmarks.smtp.client.messages;

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
