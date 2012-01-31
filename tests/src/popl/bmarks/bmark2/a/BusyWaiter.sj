//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/a/BusyWaiter.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark2.a.BusyWaiter false 10 10

package popl.bmarks.bmark2.a;

import java.util.*;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

public class BusyWaiter 
{	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int size = Integer.parseInt(args[1]);
		int len = Integer.parseInt(args[2]);
		
		BusyWaitChannel aToB = new BusyWaitChannel(len + 1);
		BusyWaitChannel bToA = new BusyWaitChannel(len);
		
		aToB.write(new Message(size));
		
		Peer a = new Peer(debug, aToB, bToA, len);				
		Peer b = new Peer(debug, bToA, aToB, len);
		
		long start = System.nanoTime();
		
		a.start();
		b.start();
		
		a.join();
		b.join();
		
		long finish = System.nanoTime();
		
		System.out.println("Run time: " + (finish - start) / 1000 + " micros.");
		
		System.out.println("Spins: " + aToB.getSpins() + ", " + bToA.getSpins());
		System.out.println("Nospins: " + aToB.getNospins() + ", " + bToA.getNospins());
	}
}
