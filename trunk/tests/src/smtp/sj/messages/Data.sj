//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/Data.sj -d tests/classes/ 

package smtp.sj.messages;

public class Data extends SmtpCommand
{	
	public static final String DATA_COMMAND = "DATA";
	
	public String command()
	{
		return DATA_COMMAND;
	}
	
	public String body()
	{
		return "";
	}
}
