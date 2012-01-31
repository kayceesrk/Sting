package ecoop.bmarks.smtp.client.messages;

public class RcptAckBody implements SmtpParseable
{
	private String msg;
	
	public RcptAckBody(String msg)
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
	
	public SmtpParseable parse(String m)
	{
		return new RcptAckBody(SmtpAck.removeTrailingLineFeed(m));
	}		
}
