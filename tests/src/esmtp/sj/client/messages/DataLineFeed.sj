//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/DataLineFeed.sj -d tests/classes/ 

package esmtp.sj.client.messages;

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
