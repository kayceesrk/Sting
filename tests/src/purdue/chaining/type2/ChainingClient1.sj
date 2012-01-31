// To test, run Client2 and Client3 first
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/chaining/type2/ChainingClient1.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.chaining.Client1  
package purdue.chaining;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client1{
       participant client1;	
	private final noalias protocol invitation {
		participants: client1, client2, client3		
		.client1: begin
		.client1->client2: <String>
		.client1->client2: <Integer>
		.client1->client3: <String>
		.client2->client1: <String>
		.client1->client2: <Double>
		.client1->client2: <Double>
		.client1: [
			  client1->client2: <Integer>
			  .client1->client3: <String>
			  .client1: {ODD: client1->client2: <String>, EVEN: client1->client2:<String>}	
			  ]*  
}
	
	public void run(int singleSession) throws Exception {
		
		final noalias SJService c = SJService.create(invitation, "localhost", 1000);

		int[][] costsMap = new int[3][3];
                //I don't know why array initialization does not work, so I have to do it in this way!!
                costsMap[0][0] = 0;                     //Cost of sending from client1 to clients 1, 2 & 3, respectively
                costsMap[0][1] = 1;
                costsMap[0][2] = 3;
                costsMap[1][0] = 1;                     //Cost of sending from client2 to clients 1, 2 & 3, respectively
                costsMap[1][1] = 0;
                costsMap[1][2] = 1;
                costsMap[2][0] = 3;                     //Cost of sending from client3 to clients 1, 2 & 3, respectively
                costsMap[2][1] = 1;
                costsMap[2][2] = 0;

		c.participantName("client1");
		c.addParticipant("client2", "localhost", 20102);		
		c.addParticipant("client3", "localhost", 20103);		
		c.setCostsMap(costsMap);                // This will enable chaining optimization. Should be called before request()
		final noalias SJSocketGroup ps;

		try (ps) 
		{
			ps = c.request();
			System.out.println("Client1 is connected to all participants");		
			ps.send("Hello, Client2 from Client1. I will send you an Integer and a Double:", "client2");
			Thread.currentThread().sleep(500);
			ps.send(new Integer(2011), "client2");			
			ps.send("Hello, Client3 from Client1.", "client3");
			String str = (String)ps.receive("client2");
			System.out.println("Client1 received: " + str);
			System.out.println("Client1 Finished.");   
			ps.send(new Double(3.14), "client2");
			ps.send(new Double(1.11), "client2");
			int i = 0;
			ps.outwhile(i < 10)
			{
				System.out.println("sending: " + i);
				ps.send(new Integer(i), "client2");
				ps.send("Loop send to client3 from client1.", "client3");
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