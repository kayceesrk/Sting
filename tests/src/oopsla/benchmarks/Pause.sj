//$ bin/sessionjc tests/src/benchmarks/Pause.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.Pause 2000

package benchmarks;

import java.io.*;

public class Pause 
{
	public static void main(String[] args) throws Exception
	{
		Thread.sleep(Integer.parseInt(args[0]));
	}
}
