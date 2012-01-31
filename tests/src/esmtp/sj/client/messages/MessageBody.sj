//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/MessageBody.sj -d tests/classes/ 

package esmtp.sj.client.messages;

public class MessageBody extends SmtpMessage
{
	public static final String MESSAGEBODY_SUFFIX = CARRIAGE_RETURN_LINE_FEED + "." + CARRIAGE_RETURN_LINE_FEED;
	
	private String body;
	
	public MessageBody(String body)
	{
		this.body = body;
	}
	
	public String prefix()
	{
		return "";
	}
	
	public String body()
	{
		return body;
	}
	
	public String suffix()
	{
		return super.suffix() + MESSAGEBODY_SUFFIX; 
	}
}
