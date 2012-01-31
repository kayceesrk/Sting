//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/Mail.sj -d tests/classes/ 

package smtp.sj.messages;

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
