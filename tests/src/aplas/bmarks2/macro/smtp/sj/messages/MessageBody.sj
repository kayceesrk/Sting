package ecoop.bmarks2.macro.smtp.sj.messages;

public class MessageBody extends SmtpMessage
{
	public static final String MESSAGE_BODY_SUFFIX = SmtpMessage.CARRIAGE_RETURN_LINE_FEED + "." + SmtpMessage.CARRIAGE_RETURN_LINE_FEED;
	
	public MessageBody(String body)
	{
		super(body);
	}
	
	public boolean isParseableFrom(String m)
	{
		return m.endsWith(MESSAGE_BODY_SUFFIX);
	}
	
	public SmtpMessage parse(String m)
	{
		return new MessageBody(m.substring(0, m.length() - MESSAGE_BODY_SUFFIX.length()));
	}
	
	public String format()
	{
		return content() + MESSAGE_BODY_SUFFIX;
	}	
}
