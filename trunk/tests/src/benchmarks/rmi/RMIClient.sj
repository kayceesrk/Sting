//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/rmi/RMIClient.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.rmi.RMIClient localhost RMIBenchmarkObject 2 10 1
//$ tests/src/benchmarks/rmi/rmi.sh localhost RMIBenchmarkObject 1 1

package benchmarks.rmi;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import benchmarks.BigObject;

public class RMIClient 
{
	public RMIClient(String rmiRegHost, String remoteObject, int depth, int singleSession, int manySessions) throws Exception
	{
		//dummyrun();
	
		Registry registry = LocateRegistry.getRegistry(rmiRegHost);
		BinaryTree theTree = BinaryTree.createBinaryTree(depth);	
		
		RMIServer remoteServer = (RMIServer) registry.lookup(remoteObject);

		theTree = remoteServer.inc(BinaryTree.createBinaryTree(0)); // Dummy run.
		
		boolean foo = true;
		
		long timeStarted = 0;		
		long timeFinished = 0;
		
		long runningTimeManySessions = 0;
		long runningTimeOneSession = 0;
		
		for(int i = 1; i <= manySessions; i++){
		
			timeStarted = System.nanoTime();
			
			for (int k = 1; k <= singleSession; k++) 
			{
				
				//theTree.print();		
				theTree = remoteServer.inc(theTree);
				
				//BigObject bo = null;
				
				/*if (msgSize == 100)
				{
					bo = remoteServer.getBigObject100(true);
				}
				else if (msgSize == 10000)
				{
					bo = remoteServer.getBigObject10000(true);
				}
				else
				{
					throw new RuntimeException("Bad BigObject size: " + msgSize);
				}*/
	
				/*if (foo)
				{
					System.out.println("Received: " + bo);
					foo = false;
				}*/
			}
	
			timeFinished = System.nanoTime();
			
			runningTimeOneSession = (timeFinished - timeStarted) / 1000;
			runningTimeManySessions = runningTimeManySessions + runningTimeOneSession;	
	
			//System.out.println("Object size " + msgSize + ", session length " + sessLen + ": " + ((tiMath.round(((float) (timeFinished - timeStarted)) / 1000.0) + "ms.");		
			//System.out.println(Math.round(((float) (timeFinished - timeStarted)) / 1000.0));
		}
		//System.out.println("Running time: "+ runningTimeManySessions);
		System.out.println(runningTimeManySessions);
	}

	public static void main(String[] args) throws Exception
	{
		new RMIClient(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
	}
	
	private void dummyrun() throws Exception
	{
		Registry registry = LocateRegistry.getRegistry("localhost");
		
		RMIServer dummy = (RMIServer) registry.lookup("dummy");

		//dummy.setBigObjectSize(msgSize);
			
		BigObject dummybo = dummy.getDummy(true); 
	}
}
