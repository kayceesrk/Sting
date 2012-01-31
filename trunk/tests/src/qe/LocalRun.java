//$ javac -cp tests/classes/ tests/src/qe/LocalRun.java -d tests/classes/
//$ java -cp tests/classes/ qe.LocalRun true ssq 1 es 100 10 2

package qe;

import java.util.*;

import qe.channel.*;
import qe.session.*;

public class LocalRun
{
	private final boolean debug;
	private final String chan;
	private final int capacity;
	private final String session;
	private final int size; // Message size.
	private final int len; // Session length.
	private final int repeats; // Number of times to repeat the session (i.e. "inners").

	public LocalRun(boolean debug, String chan, int capacity, String session, int size, int len, int repeats)
	{
		this.debug = debug;
		this.chan = chan;
		this.capacity = capacity;
		this.session = session;
		this.size = size;
		this.len = len;
		this.repeats = repeats;
	}

	private void run() throws Exception
	{
		run(false);

		for (int i = 0; i < repeats; i++)
		{
			Thread.sleep(500); // Give some time for GC, etc.

			run(true);
		}
	}

	private void run(boolean time) throws Exception
	{
		Channel aToB = createChannel(chan, capacity);
		Channel bToA = createChannel(chan, capacity); // Channel capacites do not need to be symmetric, depending on the session type. But we can be conservative for now.

		Session a = createSession(debug, session, bToA, aToB);
		Session b = createSession(debug, session, aToB, bToA);

		long start = System.nanoTime();

		b.startRequestor(size, len); // Not a useful name: here, "requestor" means the session party that should "go first". Although it may not matter a lot who goes first.
		a.startAcceptor(size, len);

		a.join();
		b.join();

		long finish = System.nanoTime();

		if (time)
		{
			System.out.println("Run time: " + ((finish - start) / 1000) + " micros.");
			//System.out.println("Run time: " + (finish - start) + " nanos.");
		}
	}

	private static Session createSession(boolean debug, String session, Channel in, Channel out)
	{
		if (session.equals("es"))
		{
			return new ExchangeSession(debug, in, out);
		}
		else if (session.equals("ss"))
		{
			return new StreamSession(debug, in, out);
		}
		else
		{
			throw new RuntimeException("[LocalRun] Unknown session flag: " + session);
		}
	}

	private static Channel createChannel(String chan, int capacity)
	{
		if (chan.equals("gsq"))
		{
			return new GloballySynchronisedQueue(capacity);
		}
		else if (chan.equals("bq"))
		{
			return new BlockingQueue(capacity);
		}
		else if (chan.equals("ssq"))
		{
			return new SharedSemaphoresQueue(capacity);
		}
		else if (chan.equals("qecq"))
		{
			return new QECellQueue(capacity);
		}
		else
		{
			throw new RuntimeException("[LocalRun] Unknown channel flag: " + chan);
		}
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		String chan = args[1];
		int capacity = Integer.parseInt(args[2]);
		String session = args[3];
		int size = Integer.parseInt(args[4]);
		int len = Integer.parseInt(args[5]);
		int repeats = Integer.parseInt(args[6]);

		new LocalRun(debug, chan, capacity, session, size, len, repeats).run();
	}
}
