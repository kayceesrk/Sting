//$ bin/sessionjc tests/src/popl/bmarks/Pause.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.Pause 2000

package popl.bmarks;

import java.io.*;

public class Pause 
{
	public static void main(String[] args) throws Exception
	{
		Thread.sleep(Integer.parseInt(args[0]));
	}
}
