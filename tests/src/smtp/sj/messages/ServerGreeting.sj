//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/ServerGreeting.sj -d tests/classes/ 

package smtp.sj.messages;

public class ServerGreeting 
{
	private String greeting;
	
	public ServerGreeting(String greeting)
	{
		this.greeting = greeting;
	}
	
	public String toString()
	{
		return greeting;
	}
}
