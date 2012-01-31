//$ bin/sessionjc tests/src/pldi/benchmarks/Pause.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ pldi.benchmarks.Pause 2000

package pldi.benchmarks;

import java.io.*;

public class Pause 
{
	public static void main(String[] args) throws Exception
	{
		Thread.sleep(Integer.parseInt(args[0]));
	}
}
