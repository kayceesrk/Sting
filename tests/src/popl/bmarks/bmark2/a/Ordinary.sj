//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/a/Ordinary.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark2.a.Ordinary false 10 10

package popl.bmarks.bmark2.a;

import java.util.*;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

public class Ordinary 
{	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int size = Integer.parseInt(args[1]);
		int len = Integer.parseInt(args[2]);
		
		OrdinaryChannel aToB = new OrdinaryChannel();
		OrdinaryChannel bToA = new OrdinaryChannel();
		
		bToA.write(new Message(size));
		
		Peer a = new Peer(debug, aToB, bToA, len);				
		Peer b = new Peer(debug, bToA, aToB, len);
		
		long start = System.nanoTime();
		
		a.start();
		b.start();
		
		a.join();
		b.join();
		
		long finish = System.nanoTime();
		
		System.out.println("Run time: " + (finish - start) / 1000 + " micros.");
	}
}
