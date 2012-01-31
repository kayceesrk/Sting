//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark1/c/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark1.c.Kill localhost RMIBenchmarkObject  

package pldi.benchmarks.benchmark1.c;

import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;

import pldi.benchmarks.BinaryTree;

public class Kill 
{
	public static void main(String[] args) throws Exception
	{
		String rmiRegHost = args[0];
		String name = args[1];
		
		Registry registry = LocateRegistry.getRegistry(rmiRegHost);
		Server remObj = (Server) registry.lookup(name);
				
		try
		{
			remObj.shutdown();
		}
		catch (Exception x)
		{
			
		}
	}
}
