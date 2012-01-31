//$ bin/sessionjc -cp tests/classes/ tests/src/purdue/continuation_exporting/Client3.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ purdue.continuations.Client3 & 
package purdue.continuations;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
import org.apache.commons.javaflow.Continuation;

public class Client3{
participant client3;
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
                        ss = SJServerSocketImpl.create(invitation, 20103, SJParticipantInfo.TYPE_PC);
			ss.participantName("client3");
			ss.addParticipant("client2", "localhost", 20102, SJParticipantInfo.TYPE_SERVER);
			ss.addParticipant("client1", "localhost", 20101, SJParticipantInfo.TYPE_MOBILE);
                        final noalias SJSocketGroup sg;

                        try(sg)
                        {
                                sg = ss.accept("client1");
                               	sg.inwhile("client1")
				{
					sg.inbranch("client1")
					{
						case ODD:
						{
						}
						case EVEN:
						{
						}
					}				
				}
                                System.out.println("Client3 is connected to all participants");
                                String str = (String)sg.receive("client1");
				System.out.println("Client3 received string: " + str);
	
                        }
			catch(Exception ex)
			{
				System.out.println("client3 Exception: " + ex);
				ex.printStackTrace();
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