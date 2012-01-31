//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/SmtpMessage.sj -d tests/classes/ 

package smtp.sj.messages;

abstract public class SmtpMessage
{
	public static final String SMTP_MESSAGE_SUFFIX = "\n";
	
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
