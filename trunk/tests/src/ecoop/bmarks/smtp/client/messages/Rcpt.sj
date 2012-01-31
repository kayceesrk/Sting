package ecoop.bmarks.smtp.client.messages;

public class Rcpt extends SmtpCommand
{
	public static final String RCPT_COMMAND = "RCPT TO:";
	
	private String recipient;
	
	public Rcpt(String recipient)
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
