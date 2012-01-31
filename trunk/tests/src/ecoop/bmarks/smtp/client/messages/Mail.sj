package ecoop.bmarks.smtp.client.messages;

public class Mail extends SmtpCommand
{
	public static final String MAIL_COMMAND = "MAIL FROM:";
	
	private String sender;
	
	public Mail(String sender)
	{
		this.sender = sender;
	}
	
	public String command()
	{
		return MAIL_COMMAND;
	}
	
	public String body()
	{
		return "<" + sender + ">";
	}
}
