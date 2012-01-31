//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/Data.sj -d tests/classes/ 

package esmtp.sj.client.messages;

public class Data extends SmtpCommand
{	
	public static final String DATA_COMMAND = "DATA";
	
	public String command()
	{
		return DATA_COMMAND;
	}
	
	public String prefix() // Don't need a space. 
	{
		return command();
	}
	
	public String body()
	{
		return "";
	}
}
