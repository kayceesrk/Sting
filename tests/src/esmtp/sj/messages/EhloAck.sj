//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/messages/EhloAck.sj -d tests/classes/ 

package esmtp.sj.messages;

public class EhloAck extends SmtpMessage
{
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED;
	
	public EhloAck(String msg)
	{
		super(msg);
	}
		
	public boolean isParseableFrom(String m)
	{
		return m.endsWith(suffix);
	}
	
	//public EhloAck parse(String m) // Annoying: covariant return types not supported until Java 5.
	public SmtpMessage parse(String m) 
	{
		return new EhloAck(SmtpMessage.removeLineFeedSuffix(m));
	}
	
	public String format()
	{
		return content() + suffix;
	}
}
