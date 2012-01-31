//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/D.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Alice  
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
//import benchmark1.BinaryTree;
public class Alice{
	
/*	private final noalias gprotocol AliceBob
	{
		participants: alice| bob.
		alice: begin.
		alice -> bob: <String>.
		bob -> aliceL <String>.
	}*/
	private final noalias lprotocol alice@AliceBob
	{
		!begin
		.bob : !<String>
		.carol:!<String>
		.bob:?(String)
		.carol:?(String)
		.bob:?[
				bob:!<String>
				.bob:?{
					INVITE: bob:?(String), 
					NOOP:
					}
			]*
	}	
	
	public void run(int singleSession) throws Exception {
		
		final noalias SJService c = SJService.create(alice, "localhost", 1000);
		c.addParticipant("carol", "localhost", 8889);
		c.addParticipant("bob", "localhost", 20101);		
		//final noalias SJSocket bobSocket, carolSocket;		
		final noalias SJSocketGroup ps;
		try (ps) 
		{
			ps = c.request(5);
			System.out.println("Alice is connected to all participants");		
			ps.send("Hello, Bob from Alice", "bob");
			ps.send("Hello, Carol from Alice", "carol");
			String recvdBob = (String)ps.receive("bob");
			System.out.println("Alice received from Bob: " + recvdBob);
			String recvdCarol = (String)ps.receive("carol");
			System.out.println("Alice received from Carol: " + recvdCarol);
			ps.inwhile("bob")
			{
				ps.send("hi", "bob");
				ps.inbranch("bob")
				{
					case INVITE:	
                                	{	
                				System.out.println("Bob received inbranch BRANCH1 from alice");
						String a = (String) ps.receive("bob");
                                	}
					 case NOOP:{}
				}
			}
			
		}
		finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Alice a = new Alice();
		
		a.run(1);
	}
}