//$ bin/sessionjc -cp tests/classes/ tests/src/smtp/sj/messages/DataAck.sj -d tests/classes/ 

package smtp.sj.messages;

public class DataAck
{
	private String msg;
	
	public DataAck(String msg)
	{
		this.msg = msg;
	}
	
	public String toString()
	{
		return msg;
	}
}
