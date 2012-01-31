package ecoop.bmarks.smtp.messages;

public class MessageBodyAck extends SmtpMessage
{
	public static final String MESSAGE_BODY_REPLY_CODE = "250";
	
	private static final String prefix = MESSAGE_BODY_REPLY_CODE;
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED; 
	
	public MessageBodyAck(String msg)
	{
		super(msg);
	}
	
	public boolean isParseableFrom(String m)
	{
		return m.startsWith(prefix) && m.endsWith(suffix);
	}
	
	public SmtpMessage parse(String m)
	{
		return new MessageBodyAck(m.substring(prefix.length(), m.length() - suffix.length()));
	}
	
	public String format()
	{
		return prefix + SmtpMessage.SPACE_SEPARATOR + content() + suffix;  
	}
}
