//$ bin/sessionjc -sourcepath tests/src/esmtp/sj/client/messages tests/src/esmtp/sj/client/messages/CompileMessagesHack.sj -d tests/classes/ 

package esmtp.sj.client.messages;

// Convenience hack to get around a compiler bug for compiling multiple classes in subpackages. 
public class CompileMessagesHack
{
	private SmtpMessage smtpMessage;
	private SmtpCommand smtpCommand;
	private SmtpAck smtpAck;
	private SmtpParseable smtpParseable;
	
	private Data data;
	private DataAck dataAck;
	private DataAckBody dataAckBody;
	private DataLineFeed dataLineFeed;
	private Ehlo ehlo;
	private EhloAckBody ehloAckBody;
	private Mail mail;
	private MailAckBody mailAckBody;
	private MessageBody messageBody;
	private MessageBodyAck messageBodyAck;
	private Quit quit;
	private QuitAck quitAck;
	private Rcpt rcpt;
	private RcptAckBody rcptAckBody;
	private RcptTo rcptTo;
	private ReplyCodeSecondAndThirdDigits replyCodeSecondAndThirdDigits;
	private ServerGreeting serverGreeting;

	public CompileMessagesHack()
	{

	}
}
