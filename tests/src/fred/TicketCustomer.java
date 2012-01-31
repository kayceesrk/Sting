import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;

import sj.runtime.*;
import sj.runtime.net.*;

class TicketCustomer {
	
	public TicketCustomer(String server, int port) throws Exception
	{	
		String TRAVEL_METHOD = "Paris by Eurostar";
		int MAX_PRICE = 115;
		Address ADDRESS = new Address("The Taj Mahal");
		
		
		//This is just a dummy protocol to get things started...
		SJProtocol p = new SJProtocol("H4sIAAAAAAAAAIWQMUvEMBTHX6snJ4KIcCB44iQ3KM3mctMJgmjAw+pcYhpq" +
			       "Si6NTTx6cDjrJ3Fx\nd3R39Cvo4OI3cDBt7lSK0AyBvLz///f+7/ETWjqHPZ" +
			       "0GZqKYru7sMmXU6EAzrV01PD5gCZfn9hFR\ncMfzAYoceo3SusrH0KJXXMQG" +
			       "elinqOpDf9ToR42cuo9hSZGcSXMNt+BbakdlYpKIzMzYjjHYebrA\nH8/bbr" +
			       "LNf3pOK0JEv/a7W6+D9+UF8DC0Vaa54Zk0sIHnInRjuEDD2U+/UKqwi9ptTB" +
			       "syGdcTW8bK\nyPaQhJVfBjq/GJezSlmU6byKEzRyzhhlfMwaUKVje+6rlHXu" +
			       "1rYyFISyo0zELI/ow9vL9PDk/q7y\nWZRkZGddxykZEySITFBoci4TuwwDa2" +
			       "U1KKuBq6qSsqq+AekRcXhSAgAA");
		
		SJServerAddress c_ca = SJServerAddress.create(p, server, port);
		SJAbstractSocket s_ca = SJNewSocket.create(c_ca, 
				SJParameters.delegator(new SJBoundedForwardingDelegator())
		);
		
		boolean decided = false;
        int retry = 3;
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Any key to start: ");
			br.readLine();
			
			s_ca.request();
			while(s_ca.outsync(!decided && retry-- > 0)){
				System.out.println("Requesting: " + TRAVEL_METHOD);
				s_ca.send(TRAVEL_METHOD);
				int quote = s_ca.receiveInt();
				System.out.println("Quoted: " + quote);
				if(quote < MAX_PRICE) decided = true;
			}
            if (retry >= 0) {
                {
                    s_ca.outlabel("ACCEPT");
                    System.out.println("Quote accepted.");
                    s_ca.send(ADDRESS);
                    System.out.println("Received dispatch date: " +
                                       (Date) s_ca.receive());
                }
            } else {
                {
                    s_ca.outlabel("REJECT");
                    System.out.println("Quote rejected.");
                }
            }
			s_ca.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args) throws Exception
	{
		new TicketCustomer(args[0], Integer.parseInt(args[1]));
	}	
}
