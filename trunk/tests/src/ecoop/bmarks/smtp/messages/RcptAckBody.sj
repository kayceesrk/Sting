package ecoop.bmarks.smtp.messages;

public class RcptAckBody extends SmtpMessage
{	
	private static final String prefix1 = SmtpMessage.HYPHEN_SEPARATOR;
	private static final String prefix2 = SmtpMessage.SPACE_SEPARATOR;
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED;
	
	public RcptAckBody(String msg)
	{
		super(msg);
	}

	public boolean isParseableFrom(String m)
	{
		return (m.startsWith(prefix1) || m.startsWith(prefix2)) && m.endsWith(suffix);
	}
	
	public SmtpMessage parse(String m)
	{
		return new RcptAckBody(SmtpMessage.removeLineFeedSuffix(m)); // Keeps the prefix.
	}		
	
	public String format()
	{
		return content() + suffix;
	}
}
