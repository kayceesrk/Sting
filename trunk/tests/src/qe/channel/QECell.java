//$ javac -cp tests/classes/ tests/src/qe/channel/QECell.java -d tests/classes/

package qe.channel;

import java.util.concurrent.Semaphore;

public class QECell
{
	public Semaphore ready = new Semaphore(1); // Fairness doesn't matter (I think).
	public Message msg;

	public QECell()
	{
		try
		{
			ready.acquire();
		}
		catch (InterruptedException ie)
		{
			throw new RuntimeException(ie);
		}
	}

	public Message read()
	{
		try
		{
			ready.acquire();
		}
		catch (InterruptedException ie)
		{
			throw new RuntimeException(ie);
		}

		return msg; // No copy here.
	}

	public void write(Message msg)
	{
		this.msg = msg;

		ready.release();
	}
}
