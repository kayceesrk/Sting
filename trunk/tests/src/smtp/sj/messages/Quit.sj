//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/Quit.sj -d tests/classes/ 

package smtp.sj.messages;

public class Quit extends SmtpCommand
{
	public static final String QUIT_COMMAND = "QUIT";
	
	public String command()
	{
		return QUIT_COMMAND;
	}
	
	public String body()
	{
		return "";
	}
}
