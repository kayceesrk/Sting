// To test, run Client2 first in Client2.sj and Client3 in Client3.sj file
//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/example2/Client1.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.Client1  
package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import java.io.Serializable;
import org.apache.commons.javaflow.Continuation;

public class Client1 implements Runnable, Serializable{


	public void run()  {
	}
	
	static class myRunnable implements Runnable, Serializable   //We Must make the class serializable in order to be able to transfer continuation
	{
		participant client1;	
		private final noalias protocol invitation {
		      participants: client1, client2, client3
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
		      .client1->client3: <String> 
		}

		public void run()
		{
		   try
		   {
			final noalias SJService c = SJService.create(invitation, "localhost", 1000, SJParticipantInfo.TYPE_MOBILE);
			c.participantName("client1");	//Although still not forced by the compiler, it is important to provide *this* participant's to the runtime.
										 	   
			c.addParticipant("client2", "localhost", 20102, SJParticipantInfo.TYPE_SERVER);		
			c.addParticipant("client3", "localhost", 20103, SJParticipantInfo.TYPE_PC);		
		
			final noalias SJSocketGroup ps;
			try (ps) 
			{
				ps = c.request();
				ps.continuationEnabled = true;
				System.out.println("Client1 is connected to all participants");		
				ps.send("Hello, Client2 from Client1. I will send you an Integer and a Double:", "client2");
				ps.send(new Integer(2011), "client2");
				String str = (String)ps.receive("client2");
				System.out.println("Client1 received: " + str);
				ps.send(new Double(3.14), "client2");
				ps.send(new Double(1.11), "client2");

				//We will run the loop at Client2's machine by transferring a continuation, the transfer is automatic and is determined by the network config
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
				}
				ps.send("This is continuation msg.", "client3");
			}
			catch(Exception ex)
			{
				System.out.println("client1 Exception: " + ex);
				ex.printStackTrace();
			}
			finally{}	
		   }
		   catch(Exception ex)
		   {
			System.out.println("client1 Exception: " + ex);
                        ex.printStackTrace();
                   }
		}
  	}

	public static void main(String[] args) throws Exception{
		myRunnable myClient1 = new myRunnable();
		SJSocketGroup.executeExportable(myClient1); //Instead of calling myClient1.run(), give it to this function and we'll take care of the rest
	}
}