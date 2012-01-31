//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/DataLineFeed.sj -d tests/classes/ 

package smtp.sj.messages;

public class DataLineFeed extends SmtpCommand
{	
	public String command()
	{
		return "";
	}
	
	public String body()
	{
		return "";
	}
}
