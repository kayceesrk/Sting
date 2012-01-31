//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/messages/DataAck.sj -d tests/classes/ 

package esmtp.sj.messages;

public class DataAck extends SmtpMessage
{
	public static final String DATA_REPLY_CODE = "354";
	
	private static final String prefix = DATA_REPLY_CODE + SmtpMessage.SPACE_SEPARATOR;
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED;
	
	public DataAck(String msg)
	{
		super(msg);
	}
		
	public boolean isParseableFrom(String m)
	{
		return m.startsWith(prefix) && m.endsWith(suffix);
	}
	
	public SmtpMessage parse(String m)
	{
		return new DataAck(m.substring(prefix.length(), m.length() - suffix.length()));
	}
	
	public String format()
	{
		return prefix + content() + suffix;
	}		
}
