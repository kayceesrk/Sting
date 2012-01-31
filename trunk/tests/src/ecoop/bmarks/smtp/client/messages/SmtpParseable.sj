package ecoop.bmarks.smtp.client.messages;

interface SmtpParseable
{
	boolean isParseable(String m);
	SmtpParseable parse(String m);	
}
