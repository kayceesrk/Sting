//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/messages/EmailAddress.sj -d tests/classes/ 

package esmtp.sj.messages;

public class EmailAddress extends SmtpMessage
{
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED;
	
	public EmailAddress(String addr)
	{
		super(addr);
	}	
	
	public boolean isParseableFrom(String m)
	{
		return m.endsWith(suffix);  
	}
	
	public SmtpMessage parse(String m)
	{
		return new EmailAddress(SmtpMessage.removeLineFeedSuffix(m));
	}			
	
	public String format()
	{
		return address() + suffix;
	}
	
	public String address()
	{
		return content();
	}
}
