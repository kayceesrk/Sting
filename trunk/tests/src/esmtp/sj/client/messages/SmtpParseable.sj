//$ bin/sessionjc -cp tests/classes/ tests/src/esmtp/sj/client/messages/SmtpParseable.sj -d tests/classes/ 

package esmtp.sj.client.messages;

interface SmtpParseable
{
	boolean isParseable(String m);
	SmtpParseable parse(String m);	
}
