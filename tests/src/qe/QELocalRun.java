//$ javac -cp tests/classes/ tests/src/qe/QELocalRun.java -d tests/classes/
//$ java -cp tests/classes/ qe.QELocalRun true 1 es 100 10 2

package qe;

import java.util.*;

import qe.channel.*;
import qe.session.*;

public class QELocalRun
{
	private final boolean debug;
	private final int cells;
	private final String session;
	private final int size; // Message size.
	private final int len; // Session length.
	private final int repeats; // Number of times to repeat the session.

	public QELocalRun(boolean debug, int cells, String session, int size, int len, int repeats)
	{
		this.debug = debug;
		this.cells = cells;
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
		QECell[] aToB = createCells(cells);
		QECell[] bToA = createCells(cells); // Number of cells does not need to be symmetric, depending on the session type. But we can be conservative for now.

		QESession a = createSession(debug, session, bToA, aToB);
		QESession b = createSession(debug, session, aToB, bToA);

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

	private static QECell[] createCells(int cells)
	{
		QECell[] cs = new QECell[cells];

		for (int i = 0; i < cells; i++)
		{
			cs[i] = new QECell();
		}

		return cs;
	}

	private static QESession createSession(boolean debug, String session, QECell ins[], QECell outs[])
	{
		if (session.equals("es"))
		{
			return new QEExchangeSession(debug, ins, outs);
		}
		else if (session.equals("ss"))
		{
			return new QEStreamSession(debug, ins, outs);
		}
		else
		{
			throw new RuntimeException("[QELocalRun] Unknown session flag: " + session);
		}
	}

	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		int cells = Integer.parseInt(args[1]);
		String session = args[2];
		int size = Integer.parseInt(args[3]);
		int len = Integer.parseInt(args[4]);
		int repeats = Integer.parseInt(args[5]);

		new QELocalRun(debug, cells, session, size, len, repeats).run();
	}
}
