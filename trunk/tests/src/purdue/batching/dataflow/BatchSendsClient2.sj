//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/batching/typedriven/BatchSendsClient2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.batching.Client6 & 
package purdue.batching;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

 public class Client6{
participant client2;
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
				.client1:{ODD: client1->client2: <String>, EVEN: client1->client2:<String>}
			  ]*
}
	public void run() throws Exception 
	{		
                final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(invitation, 20102);
			ss.participantName("client2");
			ss.addParticipant("client1", "localhost", 20102);			
                        final noalias SJSocketGroup client1Socket;

                        try(client1Socket)
                        {
                                client1Socket = ss.accept("client1");
                                //System.out.println("Client2 accepted connection request from Client1");
                                String str = (String)client1Socket.receive("client1");
				//System.out.println("String: " + str);
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
			finally {}
                }
                finally{}

	}
	
	public static void main(String[] args) throws Exception
	{
		Client6 a = new Client6();
		a.run();
	}
}