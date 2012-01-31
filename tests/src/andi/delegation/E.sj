//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/E.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Bob
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

//import benchmark1.BinaryTree;

public class Bob{
        private final noalias gprotocol AliceBob
        {
                alice: begin.
                participants: alice | bob.
                alice -> bob: <String>.
                bob -> alice: <String>
        }
	
	private final noalias lprotocol bob@AliceBob
	{
		alice:?begin.alice:?(String).alice:!<String>
		//.!{INVITE: , NOOP: }
		.alice: ?[alice:!<Integer>]*
		.alice: ?{BRANCH1:, NOOP2:}
	}		
	
	public void run() throws Exception{
		
		final noalias SJServerSocket ss;
		try(ss)
		{
			ss = SJServerSocketImpl.create(bob, 20101);
			
			final noalias SJSocket aliceSocket;
			
			try(aliceSocket)
			{
				//System.out.println("Run: " + run++);
				aliceSocket = ss.accept("alice");
				System.out.println("Bob accepted Alice's connection");
				//aliceSocket.inwhile()
				{					
				 	//aliceSocket.send("Hello Alice from Bob", "alice");
					String recvd = (String)aliceSocket.receive("alice");
					System.out.println("Bob received: " + recvd);
				 	aliceSocket.send("Hello Alice from Bob", "alice");
					//aliceSocket.outbranch(INVITE){}
					aliceSocket.inwhile("alic")
					{
						aliceSocket.send(new Integer(5), "alice");
					}
					aliceSocket.inbranch()
					{
						case BRANCH1:
						{
						     System.out.println("Bob received inbranch BRANCH1 from alice");
						}
						case NOOP2:{}
					}
				}
			}
			finally{}
		}
		finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Bob b = new Bob();
		b.run();
	}
}