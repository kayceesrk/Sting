//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/DataAckBody.sj -d tests/classes/ 

package esmtp.sj.client.messages;

// Not currently used. We didn't check whether the DATA ack is good or bad yet (e.g. if no valid RCPT TO has been given earlier).
public class DataAckBody implements SmtpParseable
{
	private String msg;
	
	public DataAckBody(String msg)
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
		return new DataAckBody(SmtpAck.removeTrailingLineFeed(m));
	}		
}
