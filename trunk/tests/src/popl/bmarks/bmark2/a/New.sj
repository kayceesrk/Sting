//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/a/New.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark2.a.New false 10 10

package popl.bmarks.bmark2.a;

import java.util.*;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

public class New 
{	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int size = Integer.parseInt(args[1]);
		int len = Integer.parseInt(args[2]);
		
		NewChannel aToB = new NewChannel(len + 1);
		NewChannel bToA = new NewChannel(len);
		
		aToB.write(new Message(size));
		
		Peer a = new Peer(debug, aToB, bToA, len);
		Peer b = new Peer(debug, bToA, aToB, len);
		
		/*NewChannel aToB = new NewChannel(len);
		NewChannel bToA = new NewChannel(len + 1);
		
		Peer a = new Peer(debug, aToB, bToA, len, true, size);
		Peer b = new Peer(debug, bToA, aToB, len);*/
		
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
