//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/delegation3/Bob.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.delegation3.Bob 8888
package benchmarks.delegation3;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmark1.BinaryTree;

public class Bob{
	
	private final noalias protocol p_carol 
	{
		sbegin.?[?(BinaryTree).!<BinaryTree>]*
	}		
	
	public void run(int port) throws Exception{
		
		//final noalias SJService c = SJService.create(p_carol, carol_host, carol_port);
		//final noalias SJSocket ds;
		
		final noalias SJServerSocket ss;
		try(ss){
				ss = SJServerSocketImpl.create(p_carol, port);
				while(true){
					noalias BinaryTree theTree;
					//final noalias SJSocket alice;
					final noalias SJSocket carol;
					try(carol){
									
						carol = ss.accept();
					
						//System.out.println("Run: " + run++);				
						//alice = (?[?(BinaryTree).!<BinaryTree>]*)carol.receive();
							
						carol.inwhile(){
								
								theTree = (BinaryTree)carol.receive();
							 	theTree.print();
							 	theTree.inc();
							 	carol.send(theTree);
						}
					}finally{}
				}
		}finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Bob b = new Bob();
		b.run(Integer.parseInt(args[0]));
	}
}