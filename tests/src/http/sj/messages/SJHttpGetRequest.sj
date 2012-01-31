//$ bin/sessionjc -cp tests/classes/ tests/src/http/sj/messages/SJHttpGetRequest.sj -d tests/classes/ 

package http.sj.messages;

public class SJHttpGetRequest
{
	public static final String GET_HEADER = "GET ";
	
	private String path;
	
	public SJHttpGetRequest(String path)
	{
		this.path = path;
	}
	
	public String toString()
	{
		return GET_HEADER + path + "\n\n";
	}
}
