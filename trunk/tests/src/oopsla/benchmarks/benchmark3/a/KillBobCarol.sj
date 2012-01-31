//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark3/a/KillBobCarol.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark3.a.KillBobCarol localhost 8888 localhost 9999

/**
 * 
 */
package benchmarks.benchmark3.a;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.*;

/**
 * Takes different arguments to benchmarks.benchmark3.c.KillBobCarol.
 */
public class KillBobCarol 
{	
	private static final noalias protocol p { cbegin }
	
	public static void main(String args[]) throws Exception 
	{
		Kill.main(new String[] { args[0], args[1] });		
		Kill.main(new String[] { args[2], args[3] });
	}
}
