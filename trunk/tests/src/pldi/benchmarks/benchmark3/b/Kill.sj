//$ bin/sessionjc -cp tests/classes/ tests/src/pldi/benchmarks/benchmark3/b/Kill.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.benchmark3.b.Kill localhost 8888 0 localhost 9999

/**
 * 
 */
package pldi.benchmarks.benchmark3.b;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import pldi.benchmarks.BinaryTree;

public class Kill 
{	
	private static final noalias protocol p { cbegin }
	
	public static void main(String args[]) throws Exception 
	{
		String bob = args[0];
		int bob_port = Integer.parseInt(args[1]);
		
		// FIXME: args[2] is "depth" (from alice.sh)
		
		String carol = args[3];
		int carol_port = Integer.parseInt(args[4]);
					
		final noalias SJService c_bob = SJService.create(p, bob, bob_port + 1);

		final noalias SJSocket s_bob;			
			
		try (s_bob) 
		{
			s_bob = c_bob.request(); 	
		}		
		catch (Exception x)
		{
			x.printStackTrace();
		}		
		finally 
		{
			 		
		}		
		
		final noalias SJService c_carol = SJService.create(p, carol, carol_port + 1);

		final noalias SJSocket s_carol;			
			
		try (s_carol) 
		{
			s_carol = c_carol.request(); 	
		}		
		catch (Exception x)
		{
			x.printStackTrace();
		}		
		finally 
		{
			 		
		}				
	}
}
