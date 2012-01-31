package ecoop.bmarks.smtp.messages;

public class QuitAck extends SmtpMessage
{
	public static final String QUIT_REPLY_CODE = "221";
	
	private static final String prefix = QUIT_REPLY_CODE + SmtpMessage.SPACE_SEPARATOR;
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED;	
	
	public QuitAck(String msg)
	{
		super(msg);
	}
		
	public boolean isParseableFrom(String m)
	{
		return m.startsWith(prefix) && m.endsWith(suffix);
	}
	
	public SmtpMessage parse(String m)
	{
		return new QuitAck(m.substring(prefix.length(), m.length() - suffix.length()));
	}
	
	public String format()
	{
		return prefix + content() + suffix;
	}		
}
