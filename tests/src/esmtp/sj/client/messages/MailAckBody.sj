//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/MailAckBody.sj -d tests/classes/ 

package esmtp.sj.client.messages;

public class MailAckBody implements SmtpParseable
{
	private String msg;
	
	public MailAckBody(String msg)
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
		return new MailAckBody(SmtpAck.removeTrailingLineFeed(m));
	}		
}
