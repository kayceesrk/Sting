// To test, run Client6 first in BatchSendsClient2. sj file
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/batching/dataflow/BatchSendsClient1.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.batching.Client1  
package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client1{
       participant client1;	
	private final noalias protocol invitation {
		participants: client1, client2		
		.client1: begin
		.client1->client2: <String>
		.client1->client2: <Integer>
		.client2->client1: <String>
		.client1->client2: <Double>
		.client1->client2: <Double>
		.client1: [
			  client1->client2: <Integer>
			  .client1: {ODD: client1->client2: <String>, EVEN: client1->client2:<String>}	
			  ]* 
}
	
	public void run(int singleSession) throws Exception {
		
		final noalias SJService c = SJService.create(invitation, "localhost", 1000);
		c.participantName("client1");
		c.addParticipant("client2", "localhost", 20102);		
		final noalias SJSocketGroup ps;
		try (ps) 
		{
			ps = c.request();
			System.out.println("Client1 is connected to all participants");		
			ps.send("Hello, Client2 from Client1. I will send you an Integer and a Double:", "client2");
			ps.send(new Integer(2011), "client2");
			String str = (String)ps.receive("client2");	//With dataflow analysis, this statement will be moved further down until before the first statement
			       	     					//that uses the str variable, thus increasing the chances of more efficient batching
			ps.send(new Double(3.14), "client2");
			System.out.println("Client1 received: " + str);
			ps.send(new Double(1.11), "client2");
			int i = 0;
			ps.outwhile(i < 10)
			{
				System.out.println("sending: " + i);
				ps.send(new Integer(i), "client2");
				if(i % 2 != 0)
				{
				     ps.outbranch(ODD)
				     {
					ps.send("Odd Number", "client2");
				     }
				}
				else
				{
				     ps.outbranch(EVEN)
				     {
					ps.send("Even Number", "client2");
				     }
				}
				     
				i = i + 1;
				//System.out.println("outwhile2");
			}
		}
		finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Client1 a = new Client1();
		
		a.run(1);
	}
}