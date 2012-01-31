package ecoop.bmarks.smtp.client.messages;

abstract public class SmtpMessage
{
	public static final String CARRIAGE_RETURN_LINE_FEED = "\r\n"; 
	public static final String SMTP_MESSAGE_SUFFIX = CARRIAGE_RETURN_LINE_FEED;
	
	public SmtpMessage()
	{

	}
	
	abstract public String prefix();	
	abstract public String body();
	
	public String suffix()
	{
		return SMTP_MESSAGE_SUFFIX;
	}
	
	public String construct()
	{
		return prefix() + body() + suffix();
	}
	
	public String toString()
	{
		return construct();
	}
}
