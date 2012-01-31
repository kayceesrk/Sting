package ecoop.bmarks.smtp.client.messages;

public class DataAck extends SmtpAck
{
	public static final String DATA_REPLY_CODE = "354";
	
	private String msg;
	
	public DataAck(String msg)
	{
		this.msg = msg;
	}
	
	public String replyCode()
	{
		return DATA_REPLY_CODE;
	}
	
	public String body()
	{
		return msg;
	}	
	
	public boolean isParseable(String m)
	{
		return m.startsWith(DATA_REPLY_CODE) && m.endsWith(SmtpMessage.CARRIAGE_RETURN_LINE_FEED);
	}
	
	public SmtpParseable parse(String m)
	{
		return new DataAck(SmtpAck.removeTrailingLineFeed(m).substring(DATA_REPLY_CODE.length()));
	}		
}
