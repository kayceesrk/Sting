//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/Bob.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Bob
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

//import benchmark1.BinaryTree;

public class Bob{
	
	private final noalias lprotocol p_carol 
	{
		sbegin.id:?(id:?[id:?(BinaryTree).id:!<BinaryTree>]*)
	}		
	
	public void run(int port) throws Exception{
		
		final noalias SJServerSocket ss;
		noalias BinaryTree theTree;
		try(ss){
			ss = SJServerSocketImpl.create(p_carol, port);
			
			final noalias SJSocket carol;
			final noalias SJSocket alice;
			
			try(carol, alice){
				//System.out.println("Run: " + run++);
				carol = ss.accept();			
				
				alice = (id:?[id:?(BinaryTree).id:!<BinaryTree>]*)carol.receive();
				
				alice.inwhile(){					
					theTree = (BinaryTree)alice.receive();
				 	theTree.print();
				 	theTree.inc();
				 	alice.send(theTree);
				}
			}finally{}
		}finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Bob b = new Bob();
		b.run(Integer.parseInt(args[0]));
	}
}