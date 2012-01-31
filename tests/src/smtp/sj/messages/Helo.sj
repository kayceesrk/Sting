//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/Helo.sj -d tests/classes/ 

package smtp.sj.messages;

public class Helo extends SmtpCommand 
{
	public static final String HELO_COMMAND = "HELO ";
	
	private String fqdn;
	
	public Helo(String fqdn)
	{
		this.fqdn = fqdn;
	}
	
	public String command()
	{
		return HELO_COMMAND;
	}
	
	public String body()
	{
		return fqdn;
	}
}
