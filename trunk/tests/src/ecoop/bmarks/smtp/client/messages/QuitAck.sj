package ecoop.bmarks.smtp.client.messages;

public class QuitAck extends SmtpAck
{
	public static final String QUIT_REPLY_CODE = "221";
	
	private String msg;
	
	public QuitAck(String msg)
	{
		this.msg = msg;
	}
	
	public String replyCode()
	{
		return QUIT_REPLY_CODE;
	}
	
	public String body()
	{
		return msg;
	}
	
	public boolean isParseable(String m)
	{
		return m.startsWith(QUIT_REPLY_CODE) && m.endsWith(SmtpMessage.CARRIAGE_RETURN_LINE_FEED);
	}
	
	public SmtpParseable parse(String m)
	{
		return new QuitAck(SmtpAck.removeTrailingLineFeed(m).substring(QUIT_REPLY_CODE.length()));
	}		
}
