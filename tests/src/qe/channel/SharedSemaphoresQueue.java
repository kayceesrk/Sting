//$ javac -cp tests/classes/ tests/src/qe/channel/SharedSemaphoresQueue.java -d tests/classes/

package qe.channel;

import java.util.concurrent.Semaphore;

public class SharedSemaphoresQueue extends BoundedChannel
{
	private Message[] buffer;

	private Semaphore empty;
	private Semaphore full;

	private int in = 0; // Next position to write (into the queue).
	private int out = 0; // Next position to read (out from the queue).

	public SharedSemaphoresQueue(int capacity)
	{
		super(capacity);

		buffer = new Message[capacity];

		empty = new Semaphore(capacity);
		full = new Semaphore(capacity);

		try
		{
			full.acquire(capacity);
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
			full.acquire();

			Message msg = buffer[out].copy();

			out = (out + 1) % capacity;

			empty.release();

			return msg;
		}
		catch (InterruptedException ie)
		{
			throw new RuntimeException(ie);
		}
	}

	public void write(Message msg)
	{
		try
		{
			empty.acquire();

			buffer[in] = msg;

			in = (in + 1) % capacity;

			full.release();
		}
		catch (InterruptedException ie)
		{
			throw new RuntimeException(ie);
		}
	}
}
