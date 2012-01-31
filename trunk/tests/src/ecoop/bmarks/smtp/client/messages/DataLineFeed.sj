package ecoop.bmarks.smtp.client.messages;

public class DataLineFeed extends SmtpCommand
{	
	public String command()
	{
		return "";
	}
	
	public String prefix() // Don't want any spaces.
	{
		return command();
	}
	
	public String body()
	{
		return "";
	}
}
