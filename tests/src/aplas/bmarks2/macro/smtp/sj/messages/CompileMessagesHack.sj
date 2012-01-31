//$ bin/sessionjc -cp tests/classes tests/src/ecoop/bmarks2/smtp/sj/messages/CompileMessagesHack.sj -d tests/classes

package ecoop.bmarks2.macro.smtp.sj.messages;

// Convenience hack to get around a compiler bug for compiling multiple classes in subpackages. 
public class CompileMessagesHack
{
	private SmtpMessage smtpMessage;
	
	private DataAck dataAck;
	private Ehlo ehlo;
	private EhloAck ehloAck;
	private EmailAddress emailAddress;
	private MailAckBody mailAckBody;
	private MessageBody messageBody;
	private MessageBodyAck messageBodyAck;
	private QuitAck quitAck;
	private RcptAckBody rcptAckBody;
	private ServerGreeting serverGreeting;

	public CompileMessagesHack()
	{

	}
}
