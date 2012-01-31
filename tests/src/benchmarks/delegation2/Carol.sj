//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/delegation2/Carol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.delegation2.Carol 9999 localhost 8888
package benchmarks.delegation2;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import benchmark1.BinaryTree;

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
		
		
		noalias BinaryTree theTree;		
		try(ss){
			
			ss = SJServerSocketImpl.create(p_alice, port);
			while(true){
				final noalias SJSocket alice;
				final noalias SJSocket dummy;
				final noalias SJSocket bob;
				
				try(dummy){
	
					//System.out.println("Run: " + run++);
					dummy = ss.accept();
	
					dummy.inwhile(){
						
						theTree = (BinaryTree)dummy.receive();
					 	//theTree.print();
					 	//System.out.println();
					 	theTree.inc();
					 	dummy.send(theTree);
					 	
					 	//System.out.println("Dummy done.");
					}
				}finally{}
				
				try(alice, bob){
	
					alice = ss.accept();
					bob = c.request();
					
					bob.passCopy(alice);
					
//					alice.inwhile(){
//						
//						theTree = (BinaryTree)alice.receive();
//					 	theTree.print();
//					 	System.out.println();
//					 	theTree.inc();
//					 	alice.send(theTree);
//				}					
					
				}finally{}
			}
		}finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Carol c = new Carol();
		c.run(Integer.parseInt(args[0]), args[1], Integer.parseInt(args[2]));
	}
}