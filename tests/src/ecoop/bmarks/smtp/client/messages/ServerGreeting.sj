package ecoop.bmarks.smtp.client.messages;

public class ServerGreeting implements SmtpParseable // Actually, the server greeting starts with a "220"; so this message can be made an SmtpAck.
{
	private String greeting;
	
	public ServerGreeting(String greeting)
	{
		this.greeting = greeting;
	}
	
	public String toString()
	{
		return greeting;
	}
	
	public boolean isParseable(String m)
	{
		return m.endsWith(SmtpMessage.CARRIAGE_RETURN_LINE_FEED);
	}
	
	public SmtpParseable parse(String m)
	{
		return new ServerGreeting(SmtpAck.removeTrailingLineFeed(m));
	}			
}
