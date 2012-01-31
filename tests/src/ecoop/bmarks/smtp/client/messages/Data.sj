package ecoop.bmarks.smtp.client.messages;

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
