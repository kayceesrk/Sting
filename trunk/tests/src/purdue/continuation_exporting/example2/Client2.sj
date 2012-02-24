//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/Client2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.Client2 & 
package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client2{
participant client2;
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
				.client1:{ODD: client1->client2: <String>, EVEN: client1->client2:<String>}
			  ]* 		
		.client1->client3: <String>
}
	public void run() throws Exception 
	{		
                final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(invitation, 20102, SJParticipantInfo.TYPE_SERVER);
			ss.participantName("client2");
			ss.addParticipant("client3", "localhost", 20103, SJParticipantInfo.TYPE_PC); 
			ss.addParticipant("client1", "localhost", 20101, SJParticipantInfo.TYPE_MOBILE);	  
                        final noalias SJSocketGroup client1Socket;
			
                        try(client1Socket)
                        {
                                client1Socket = ss.accept("client1");
                                System.out.println("Client2 accepted connection request from Client1");
                                String str = (String)client1Socket.receive("client1");
				System.out.println("String: " + str);
                                Integer ii = (Integer)client1Socket.receive("client1");
				client1Socket.send("Hi, from client2", "client1");
                                Double d = (Double)client1Socket.receive("client1");
                                Double d2 = (Double)client1Socket.receive("client1");
				System.out.println("Client2 received:\nString: " + str + "\nInteger: " + ii + "\nDouble1: " + d + "\nDouble2: " + d2);
				client1Socket.inwhile("client1")
				{
					Integer i = (Integer)client1Socket.receive("client1");
					System.out.println("Received: " + i);
					client1Socket.inbranch("client1")
					{
						case ODD:
						{
							String str3 = (String)client1Socket.receive("client1");
							System.out.println(str3);
						}
						case EVEN:
						{
							String str4 = (String)client1Socket.receive("client1");
                                             		System.out.println("Client2 received: " + str4);
						}
					}				
				}
				
                        }
			catch(Exception ex)
			{
				System.out.println("client1Socket Exception: " + ex);
				ex.printStackTrace();
			}
			finally {}
                }
                finally{}

	}
	
	public static void main(String[] args) throws Exception
	{
		Client2 a = new Client2();
		a.run();
	}
}