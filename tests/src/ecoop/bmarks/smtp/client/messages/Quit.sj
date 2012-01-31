package ecoop.bmarks.smtp.client.messages;

public class Quit extends SmtpCommand
{
	public static final String QUIT_COMMAND = "QUIT";
	
	public String command()
	{
		return QUIT_COMMAND;
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
