//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/chaining/type2/ChainingClient2.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.chaining.Client2 & 
package purdue.chaining;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client2
{
	participant client2;
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
				.client1:{ODD: client1->client2: <String>, EVEN: client1->client2:<String>}
			  ]* 
}
	public void run() throws Exception 
	{		
                final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(invitation, 20102);
			int[][] costsMap = new int[3][3];
			//I don't know why array initialization does not work, so I have to do it in this way!!
			costsMap[0][0] = 0;	 		//Cost of sending from client1 to clients 1, 2 & 3, respectively
			costsMap[0][1] = 1;
			costsMap[0][2] = 3;
			costsMap[1][0] = 1;			//Cost of sending from client2 to clients 1, 2 & 3, respectively
			costsMap[1][1] = 0;			
			costsMap[1][2] = 1;
			costsMap[2][0] = 3;			//Cost of sending from client3 to clients 1, 2 & 3, respectively
			costsMap[2][1] = 1;
			costsMap[2][2] = 0;
			ss.participantName("client2");
			ss.addParticipant("client1", "localhost", 20101); 
			ss.addParticipant("client3", "localhost", 20103);
                        final noalias SJSocketGroup sockets;

                        try(sockets)
                        {
                                sockets = ss.accept("client1");				
                                System.out.println("Client2 is connected");
				ss.setCostsMap(costsMap);	//This will enable chaining optimization. Must be called after accept().
                                String str = (String)sockets.receive("client1");
				System.out.println("String: " + str);
                                Integer ii = (Integer)sockets.receive("client1");
				System.out.println("Client 2 received from client1: " + ii);
				sockets.send("Hi client1, from client2", "client1");
                                Double d = (Double)sockets.receive("client1");
				System.out.println("Client 2 recived:" + d);
				System.out.println("Client2 Finished.");
                                Double d2 = (Double)sockets.receive("client1");
				System.out.println("Client2 received:\nString: " + str + "\nInteger: " + ii + "\nDouble1: " + d + "\nDouble2: " + d2);
				sockets.inwhile("client1")
				{
					Integer i = (Integer)sockets.receive("client1");
					System.out.println("Received: " + i);
					sockets.inbranch("client1")
					{
						case ODD:
						{
							String str3 = (String)sockets.receive("client1");
							System.out.println(str3);
						}
						case EVEN:
						{
							String str4 = (String)sockets.receive("client1");
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
		Client2 a = new Client2();
		a.run();
	}
}