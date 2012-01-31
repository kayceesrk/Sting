package ecoop.bmarks.smtp.client.messages;

abstract public class SmtpCommand extends SmtpMessage
{
	abstract public String command();	
	
	public String prefix()
	{
		return command() + " "; // Unlike SmtpAcks, SmtpCommands include the " " in the prefix, since it will always be a " " (i.e. not a "-", as in multi-line acks.)
	}
}
