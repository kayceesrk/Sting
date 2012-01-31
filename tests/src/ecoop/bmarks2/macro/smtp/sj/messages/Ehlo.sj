package ecoop.bmarks2.macro.smtp.sj.messages;

public class Ehlo extends SmtpMessage
{
	public static final String EHLO_COMMAND = "EHLO";
	
	private static final String prefix = EHLO_COMMAND + SmtpMessage.SPACE_SEPARATOR;
	private static final String suffix = SmtpMessage.CARRIAGE_RETURN_LINE_FEED;

	public Ehlo(String fqdn)
	{
		super(fqdn);
	}
		
	public boolean isParseableFrom(String m)
	{
		return m.startsWith(prefix) && m.endsWith(suffix);
	}
	
	public SmtpMessage parse(String m)
	{		
		return new Ehlo(m.substring(prefix.length(), m.length() - suffix.length()));
	}
		
	public String format()
	{
		return prefix + content() + suffix;
	}
}
