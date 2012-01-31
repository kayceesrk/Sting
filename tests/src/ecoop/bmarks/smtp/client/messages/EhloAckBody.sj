package ecoop.bmarks.smtp.client.messages;

public class EhloAckBody implements SmtpParseable
{
	private String msg;
	
	public EhloAckBody(String msg)
	{
		this.msg = msg;
	}
		
	public String toString()
	{
		return msg;
	}
	
	public boolean isParseable(String m)
	{
		return m.endsWith(SmtpMessage.CARRIAGE_RETURN_LINE_FEED);
	}
	
	//public EhloAckBody parse(String m) // Annoying: covariant return types not supported until Java 5.
	public SmtpParseable parse(String m) 
	{
		return new EhloAckBody(SmtpAck.removeTrailingLineFeed(m));
	}	
}
