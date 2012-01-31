import java.util.Random;

import sj.runtime.SJProtocol;
import sj.runtime.SJServerAddress;
import sj.runtime.net.SJAbstractServerSocket;
import sj.runtime.net.SJAbstractSocket;
import sj.runtime.net.SJBoundedForwardingDelegator;
import sj.runtime.net.SJParameters;
import sj.runtime.net.SJNewServerSocket;
import sj.runtime.net.SJNewSocket;

class TicketAgency {
	
	public TicketAgency(int port, String addr_s, int port_s) throws Exception
	{	
		SJProtocol p_ac = new SJProtocol("H4sIAAAAAAAAAIWQMUvEMBTHX6snJ4KIcCB44iQ3KM3mctMJgmjAw+pcYhpq" +
			       "Si6NTTx6cDjrJ3Fx\nd3R39Cvo4OI3cDBt7lSK0AyBvLz///f+7/ETWjqHPZ" +
			       "0GZqKYru7sMmXU6EAzrV01PD5gCZfn9hFR\ncMfzAYoceo3SusrH0KJXXMQG" +
			       "elinqOpDf9ToR42cuo9hSZGcSXMNt+BbakdlYpKIzMzYjjHYebrA\nH8/bbr" +
			       "LNf3pOK0JEv/a7W6+D9+UF8DC0Vaa54Zk0sIHnInRjuEDD2U+/UKqwi9ptTB" +
			       "syGdcTW8bK\nyPaQhJVfBjq/GJezSlmU6byKEzRyzhhlfMwaUKVje+6rlHXu" +
			       "1rYyFISyo0zELI/ow9vL9PDk/q7y\nWZRkZGddxykZEySITFBoci4TuwwDa2" +
			       "U1KKuBq6qSsqq+AekRcXhSAgAA");
		
		String p_as_str = "H4sIAAAAAAAAAIWQMUvEMBTHX6snJ4KIcCB44iQ3KM3mctMJgmjAw+pcYhpq" +
			       "Si6NTTx6cDjrJ3Fx\nd3R39Cvo4OI3cDBt7lSK0AyBvLz///f+7/ETWjqHPZ" +
			       "0GZqKYru7sMmXU6EAzrV01PD5gCZfn9hFR\ncMfzAYoceo3SusrH0KJXXMQG" +
			       "elinqOpDf9ToR42cuo9hSZGcSXMNt+BbakdlYpKIzMzYjjHYebrA\nH8/bbr" +
			       "LNf3pOK0JEv/a7W6+D9+UF8DC0Vaa54Zk0sIHnInRjuEDD2U+/UKqwi9ptTB" +
			       "syGdcTW8bK\nyPaQhJVfBjq/GJezSlmU6byKEzRyzhhlfMwaUKVje+6rlHXu" +
			       "1rYyFISyo0zELI/ow9vL9PDk/q7y\nWZRkZGddxykZEySITFBoci4TuwwDa2" +
			       "U1KKuBq6qSsqq+AekRcXhSAgAA";
		
		SJProtocol p_as = new SJProtocol(p_as_str);
		
		SJAbstractServerSocket ss = null;
		
		try
		{		
			ss = SJNewServerSocket.create(p_ac, port, 
				SJParameters.delegator(new SJBoundedForwardingDelegator())
			);
			
			while(true)
			{
				SJAbstractSocket s_ac = null;
				
				try{
					s_ac = ss.accept();
					
					while(s_ac.insync()){
						String details = (String) s_ac.receive();
						System.out.print("Request: " + details);
						int price = getPrice(details);
						System.out.println(", cost: " + price);
						s_ac.sendInt(price);
					}
					
					String label = s_ac.inlabel();
					if(label.equals("ACCEPT")){
						System.out.println("Quote accepted : 'ere be delegation!");
						
						SJServerAddress c_as = SJServerAddress.create(p_as, addr_s, port_s);
						SJAbstractSocket s_as = SJNewSocket.create(c_as, 
							SJParameters.delegator(new SJBoundedForwardingDelegator())
						);
						
						 s_as.request();
                         s_as.sendSession(s_ac, p_as_str);
                         
                         s_as.close();
						
//						Address address = (Address) s_ac.receive();
//						System.out.println("Send to " + address);
//						s_ac.send(new Date());
					}else{
						System.out.println("Quotes rejected");
					}
					
					s_ac.close();
					
					System.out.println("Client disconnected");
				}
				finally
				{
					
				}
			}
			
		} 
		finally 
		{
			if (ss != null) ss.close();
		}		
	}
	
	public static void main(String[] args) throws Exception 
	{
		new TicketAgency(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
	}	
	
	private int getPrice(String travelMethod) 
	{
		return new Random().nextInt(30) + 100;
	}
}
