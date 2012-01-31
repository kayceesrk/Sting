//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/delegation2/Alice.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.delegation2.Alice localhost 9999 2 1 
package benchmarks.delegation2;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import benchmark1.BinaryTree;
public class Alice{
	
	private final noalias protocol p_carol 
	{
		cbegin.![!<BinaryTree>.?(BinaryTree)]*
	}	
	
	public void run(String carol_host, int carol_port, int depth, int singleSession) throws Exception {
		
		final noalias SJService c = SJService.create(p_carol, carol_host, carol_port);
		
		//create the balanced binary tree of depth @depth
		noalias BinaryTree theTree = BinaryTree.createBinaryTree(depth);
		final noalias SJSocket ds;
		final noalias SJSocket dummy; 

		long timeStarted = 0;		
		long timeFinished = 0;
		
		//long running_mean = -1;
		//long one_iteration = -1;
				
		try (dummy) 
		{   //dummy
			dummy = c.request();
			//timeStarted = System.nanoTime();
			int k = 1;
			dummy.outwhile(k <= 100){
				//theTree.print();
				dummy.send(theTree);
				theTree = (BinaryTree)dummy.receive();
				k++;
			}
			//timeFinished = System.nanoTime();
		}finally{}
		theTree = BinaryTree.createBinaryTree(depth);

		try (ds) 
		{
			ds = c.request();
			timeStarted = System.nanoTime();
			int k = 1;
			ds.outwhile(k <= singleSession){
				theTree.print();
				ds.send(theTree);
				theTree = (BinaryTree)ds.receive();
				k++;
			}
			timeFinished = System.nanoTime();
		}finally{}
		System.out.println((timeFinished - timeStarted) / 1000);
	}
	
	public static void main(String[] args) throws Exception{
		
		Alice a = new Alice();
		
		a.run(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}
}