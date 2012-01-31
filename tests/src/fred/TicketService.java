import java.util.Date;

import sj.runtime.SJProtocol;
import sj.runtime.net.SJAbstractServerSocket;
import sj.runtime.net.SJAbstractSocket;
import sj.runtime.net.SJBoundedForwardingDelegator;
import sj.runtime.net.SJParameters;
import sj.runtime.net.SJNewServerSocket;

class TicketService {
	
	public TicketService(int port) throws Exception
	{	
		String p_sa_str = "H4sIAAAAAAAAAIWQMUvEMBTHX6snJ4KIcCB44iQ3KM3mctMJgmjAw+pcYhpq" +
	       "Si6NTTx6cDjrJ3Fx\nd3R39Cvo4OI3cDBt7lSK0AyBvLz///f+7/ETWjqHPZ" +
	       "0GZqKYru7sMmXU6EAzrV01PD5gCZfn9hFR\ncMfzAYoceo3SusrH0KJXXMQG" +
	       "elinqOpDf9ToR42cuo9hSZGcSXMNt+BbakdlYpKIzMzYjjHYebrA\nH8/bbr" +
	       "LNf3pOK0JEv/a7W6+D9+UF8DC0Vaa54Zk0sIHnInRjuEDD2U+/UKqwi9ptTB" +
	       "syGdcTW8bK\nyPaQhJVfBjq/GJezSlmU6byKEzRyzhhlfMwaUKVje+6rlHXu" +
	       "1rYyFISyo0zELI/ow9vL9PDk/q7y\nWZRkZGddxykZEySITFBoci4TuwwDa2" +
	       "U1KKuBq6qSsqq+AekRcXhSAgAA";

		SJProtocol p_sa = new SJProtocol(p_sa_str);
		
		SJAbstractServerSocket ss_sa = null;
		
		try
		{		
			ss_sa = SJNewServerSocket.create(p_sa, port,
				SJParameters.delegator(new SJBoundedForwardingDelegator())
			);
			
			while(true)
			{
				SJAbstractSocket s_sa = null;		
				SJAbstractSocket s_sc = null;
				
				try{
					s_sa = ss_sa.accept();
					
					s_sc = s_sa.receiveSession(p_sa_str);
					
					Address c_addr = (Address) s_sc.receive();
					
					System.out.println("Received address: " + c_addr);
					
					Date dispatch = new Date();
					System.out.println("Dispatching:" + dispatch);
					
					s_sc.send(dispatch);
					
					s_sc.close();
					
					s_sa.close();

				}finally{
					
				}
			}
			
		} 
		finally 
		{
			if (ss_sa != null) ss_sa.close();
		}		
	}
	
	public static void main(String[] args) throws Exception 
	{
		new TicketService(Integer.parseInt(args[0]));
	}	
}
