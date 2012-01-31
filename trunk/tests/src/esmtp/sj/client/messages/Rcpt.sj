//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/Rcpt.sj -d tests/classes/ 

package esmtp.sj.client.messages;

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
