//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/messages/SmtpMessage.sj -d tests/classes/ 

package esmtp.sj.messages;

abstract public class SmtpMessage
{
	public static final String CARRIAGE_RETURN_LINE_FEED = "\r\n";
	
	public static final String HYPHEN_SEPARATOR = "-";
	public static final String SPACE_SEPARATOR = " ";
	
	private String content;
	
	public SmtpMessage(String content)
	{
		this.content = content;
	}
	
	public String content()
	{
		return content;
	}
	
	abstract public boolean isParseableFrom(String m);
	abstract public SmtpMessage parse(String m);
	
	abstract public String format();
	
	public String toString()
	{
		return format();
	}
	
	public static final String removeLineFeedSuffix(String m)
	{
		return m.substring(0, m.length() - SmtpMessage.CARRIAGE_RETURN_LINE_FEED.length());
	}
}
