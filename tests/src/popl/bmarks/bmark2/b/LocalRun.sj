//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/b/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ popl.bmarks.bmark2.b.LocalRun false o 1 10 100 2

package popl.bmarks.bmark2.b;

import java.util.*;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

public class LocalRun 
{	
	public LocalRun()
	{
		
	}

	private void run(boolean debug, String chan, int session, int size, int len, int repeats) throws Exception
	{
		run(debug, false, chan, session, size, len);
		
		for (int i = 0; i < repeats; i++)
		{
			Thread.sleep(200); // Give some time for GC, etc.
			
			run(debug, true, chan, session, size, len);
		}
	}
	
	private void run(boolean debug, boolean timer, String chan, int session, int size, int len) throws Exception
	{
		Channel aToB = createChannel(chan, len);
		Channel bToA = createChannel(chan, len);
		
		Session a = createSession(debug, session, true, bToA, aToB);
		Session b = createSession(debug, session, false, aToB, bToA);
			
		long start = System.nanoTime();
		
		a.start(len, size);
		b.start(len, size);
		
		a.join();
		b.join();
		
		long finish = System.nanoTime();
		
		if (timer)
		{
			System.out.println("Run time: " + ((finish - start) / 1000) + " micros.");
			
			//if (chan.equals("w") || chan.equals("n") || chan.equals("r") || chan.equals("s"))
			if (aToB instanceof SpinChannel) 
			{
				System.out.println("Spins: " + ((SpinChannel) aToB).getSpins() + ", " + ((SpinChannel) bToA).getSpins());
				System.out.println("Nospins: " + ((SpinChannel) aToB).getNospins() + ", " + ((SpinChannel) bToA).getNospins());
			}
			
			//System.out.println(((finish - start) / 1000));
		}
	}

	private static Session createSession(boolean debug, int session, boolean requestor, Channel in, Channel out)
	{
		if (session == 1)
		{
			return new Session1(debug, requestor, in, out);
		}
		else if (session == 2)
		{
			return new Session2(debug, requestor, in, out);
		}
		
		return null;
	}
	
	private static Channel createChannel(String chan, int len)
	{
		if (chan.equals("o"))
		{
			return new OrdinaryChannel();
		}
		else if (chan.equals("w"))
		{
			return new BusyWaitChannel(len);
		} 
		else if (chan.equals("n"))
		{
			return new NewChannel(len);
		}
		else if (chan.equals("r"))
		{
			return new NewerChannel(len);
		}
		else if (chan.equals("s"))
		{
			return new NewestChannel(len);
		}
		else
		{
			throw new RuntimeException("[LocalRun] Unknown channel type flag: " + chan);
		}		
	}
	
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String chan = args[1];
		int session = Integer.parseInt(args[2]);
		int size = Integer.parseInt(args[3]);
		int len = Integer.parseInt(args[4]);
		int repeats = Integer.parseInt(args[5]);
		
		new LocalRun().run(debug, chan, session, size, len, repeats);
	}
}
