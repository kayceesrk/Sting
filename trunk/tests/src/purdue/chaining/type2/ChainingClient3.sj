//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/chaining/type2/ChainingClient3.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.chaining.Client3 & 
package purdue.chaining;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;

public class Client3
{
	participant client3;
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
				//client1->client2: <Integer>
				client1->client3: <String>
				.client1:{ODD: client1->client2: <String>, EVEN: client1->client2:<String>} 
			  ]* 
}
	public void run() throws Exception 
	{		
                final noalias SJServerSocket ss;
                try(ss)
                {
                        ss = SJServerSocketImpl.create(invitation, 20103);
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
			ss.participantName("client3");
			ss.addParticipant("client1", "localhost", 20101);  
			ss.addParticipant("client2", "localhost", 20102);
			final noalias SJSocketGroup sockets;

                        try(sockets)
                        {
                                sockets = ss.accept("client1");
                                System.out.println("Client3 is connected.");
				ss.setCostsMap(costsMap);  //This will enable chaining optimization. Must be called after accept().
                                String str = (String)sockets.receive("client1");
				System.out.println("Client3 received from Client1: " + str);
				System.out.println("Client3 Finished.");
				sockets.inwhile("client1")
				{
					String str2 = (String)sockets.receive("client1");
					System.out.println("Client3 received from client1: " + str2);
					sockets.inbranch("client1")
					{
						case ODD:
						{
				
						}
						case EVEN:
						{
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
		Client3 a = new Client3();
		a.run();
	}
}