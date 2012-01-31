//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/Mail.sj -d tests/classes/ 

package esmtp.sj.client.messages;

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
