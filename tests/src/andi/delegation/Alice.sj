//$ bin/sessionjc -cp tests/classes/ tests/src/andi/delegation/Alice.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ andi.delegation.Alice  
package andi.delegation;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
//import benchmark1.BinaryTree;
public class Alice{
	
	private final noalias lprotocol p_carol@id2
	{
		//cbegin.![myid:!<BinaryTree>.id2:?(BinaryTree).!{INVITE:myid:!<Integer> , NOOP:}]*
		!begin.![myid2:?{INVITE: , NOOP:}]*
	}
	/*private final noalias gprotocol p_carol2
	{
		//id2:[id1: {INVITE: , NOOP: }]*
		begin.id2:[id1: {INVITE:id1 -> id2: <Integer, BinaryTree, String> , NOOP:}]*
	}*/
	private final noalias gprotocol p1
	{
	     client:begin.
	     participants: a|b|c|d.
	infoSvr->client: <String>.
		 infoSvr->client: <Integer>.
		 infoSvr:
	[infoSvr->client: <BinaryArray>.
		  infoSvr->client: <String>.
		 infoSvr->client: <Integer>.
		 infoSvr->client: <String>.
		 client: {	
		 INVITE: client->mailSvr: <String>,
	NOOP:
	}	
	]*

	}
	

	
	
	public void run(String carol_host, int carol_port, int depth, int singleSession) throws Exception {
		
		final noalias SJService c = SJService.create(p_carol, carol_host, carol_port);
		//create the balanced binary tree of depth @depth
		noalias BinaryTree theTree = BinaryTree.createBinaryTree(depth);
		final noalias SJSocket ds;		
		
		try (ds) 
		{
			ds = c.request();		
			
			int k = 1;
			ds.outwhile(k <= singleSession){
								
				theTree.print();
				
				System.out.println();
				
//				ds.send(theTree);
			//	theTree = (BinaryTree)ds.receive();
				ds.inbranch()
				{
					case INVITE:
					{
//						ds.send(new Integer(1));
					}
					case NOOP: {}
				}
				
				k++;
			}
		}finally{}
	}
	
	public static void main(String[] args) throws Exception{
		
		Alice a = new Alice();
		
		a.run(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}
}