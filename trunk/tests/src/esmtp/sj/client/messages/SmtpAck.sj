//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/SmtpAck.sj -d tests/classes/ 

package esmtp.sj.client.messages;

abstract public class SmtpAck extends SmtpMessage implements SmtpParseable
{
	abstract public String replyCode();	
	
	public String prefix() // Would be nice if we could generate patterns for the parseMessage routine using these constants.
	{
		return replyCode(); // Unlike SmtpCommands, SmtpAck prefixes don't include a " " because multi-line replies will have a "-" instead (these can be in the body instead).
	}
	
	public static final String removeTrailingLineFeed(String m)
	{
		return m.substring(0, m.length() - SmtpMessage.CARRIAGE_RETURN_LINE_FEED.length());
	}
}
