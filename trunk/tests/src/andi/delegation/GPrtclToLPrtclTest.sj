//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/GPrtclToLPrtclTest.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Alice  
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import java.util.Hashtable;
//import benchmark1.BinaryTree;
public class Alice{
	participant client;
	private final noalias gprotocol invitation {
		participants: client, infoSvr, mailSvr		
		.client: begin
		.infoSvr->client: <String>
		.infoSvr->client: <Integer>
	 	.infoSvr:[
			infoSvr->client: <Boolean>
	 		.infoSvr->client: <String>
			.infoSvr->client: <Integer>
			.infoSvr->client: <Double>
			.infoSvr:{				
				INVITE: client->mailSvr: <Double>,
				NOOP: mailSvr:[mailSvr->infoSvr:<String>]*
			}
		]*
}
/*	private final noalias lprotocol alice@AliceBob
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
	}*/	
	
	public void run(int singleSession) throws Exception {
		
		final noalias SJService c = SJService.create(invitation, "localhost", 1000);
		c.addParticipant("carol", "localhost", 8889);
		c.addParticipant("bob", "localhost", 20101);		
		//final noalias SJSocket bobSocket, carolSocket;		
		final noalias SJSocketGroup ps;
		try (ps) 
		{
			ps = c.request(5);
			System.out.println("Alice is connected to all participants");		
			String str = (String)ps.receive("infoSvr");
			Integer ii = (Integer)ps.receive("infoSvr");
			ps.inwhile("infoSvr")
			{
				Boolean b = (Boolean)ps.receive("infoSvr");
				String s = (String)ps.receive("infoSvr");
				Integer ih = (Integer)ps.receive("infoSvr");
				Double d = (Double)ps.receive("infoSvr");
				ps.inbranch("infoSvr")
				{
					case INVITE:	
                                	{	
                				System.out.println("Bob received inbranch BRANCH1 from alice");
						ps.send(new Double(2.5), "mailSvr");
                                	}
					 case NOOP:
					 {						
					 }
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