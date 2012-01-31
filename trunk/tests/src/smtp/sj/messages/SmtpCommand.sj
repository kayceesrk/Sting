//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/SmtpCommand.sj -d tests/classes/ 

package smtp.sj.messages;

abstract public class SmtpCommand extends SmtpMessage
{
	abstract public String command();	
	
	public String prefix()
	{
		return command();
	}
}
