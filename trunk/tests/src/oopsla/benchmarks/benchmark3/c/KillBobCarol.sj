//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark3/c/KillBobCarol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark3.c.KillBobCarol localhost 8888 0 localhost 9999

/**
 * 
 */
package benchmarks.benchmark3.c;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.*;

public class KillBobCarol 
{	
	//public static final int KILL_PORT_ADJUST = 2;

	private static final noalias protocol p { cbegin }
	
	public static void main(String args[]) throws Exception 
	{
		Kill.main(new String[] { args[0], args[1] });		
		Kill.main(new String[] { args[3], args[4] }); // FIXME: args[2] is "depth" (from alice.sh)	
	}
}
