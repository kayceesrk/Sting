package ecoop.bmarks.smtp.client.messages;

public class ReplyCodeSecondAndThirdDigits implements SmtpParseable
{
	private String msg;
	
	public ReplyCodeSecondAndThirdDigits(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
	
	public boolean isParseable(String m)
	{
		return (m.length() == 2); // Could also check they are (valid) digits.
	}
	
	public SmtpParseable parse(String m)
	{
		return new ReplyCodeSecondAndThirdDigits(m);
	}	
}
