//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/F.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Carol
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

//import benchmark1.BinaryTree;

public class Carol{
        private final noalias gprotocol AliceBob
        {
                alice: begin.
                participants: alice | bob.
                alice -> bob: <String>.
                bob -> alice: <String>
        }
	
	private final noalias lprotocol carol@AliceBob
	{
		alice:?begin.alice:?(String).alice:!<String>
	}		
	
	public void run() throws Exception{
		
		final noalias SJServerSocket ss;
		try(ss)
		{
			ss = SJServerSocketImpl.create(carol, 8889);
			
			final noalias SJSocket aliceSocket;
			
			try(aliceSocket)
			{
				//System.out.println("Run: " + run++);
				aliceSocket = ss.accept();
				System.out.println("Carol accepted Alice's connection");
				//aliceSocket.inwhile()
				{					
					String recvd = (String)aliceSocket.receive("alice");
					System.out.println("Carol received: " + recvd + "\n");
				 	aliceSocket.send("Hello, Alice from Carol", "alice");
				}
			}
			finally{}
		}
		finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Carol c = new Carol();
		c.run();
	}
}