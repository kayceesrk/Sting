//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/Carol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Carol 
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
//import benchmark1.BinaryTree;

public class Carol{
	
	private final noalias protocol p_alice 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
	}		
	private final noalias protocol p_bob 
	{
		cbegin.!<?[?(BinaryTree).!<BinaryTree>]*>
	}	
	
	public void run(int port, String bob_host, int bob_port) throws Exception{
		
		final noalias SJServerSocket ss;
		final noalias SJService c = SJService.create(p_bob, bob_host, bob_port);
		final noalias SJSocket bob;		
		try(ss){
			ss = SJServerSocketImpl.create(p_alice, port);
			
			final noalias SJSocket alice;
			try(alice, bob){
				//System.out.println("Run: " + run++);
				alice = ss.accept();
				
				bob = c.request();
				
				bob.passCopy(alice);
				
			}finally{}
		}finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Carol c = new Carol();
		c.run(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
	}
}