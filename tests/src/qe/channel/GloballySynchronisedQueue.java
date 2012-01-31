//$ javac -cp tests/classes/ tests/src/qe/channel/GloballySynchronisedQueue.java -d tests/classes/

package qe.channel;

public class GloballySynchronisedQueue extends BoundedChannel
{
	private Message[] buffer;

	private int in = 0; // Next position to write (into the queue).
	private int out = 0; // Next position to read (out from the queue).

	public GloballySynchronisedQueue(int capacity)
	{
		super(capacity);

		if (capacity <= 1)
		{
			throw new RuntimeException("[GloballySynchronisedQueue] Capacity should be bigger than 1."); // A buffer space is "wasted" to distinguish empty/full buffer.
		}

		buffer = new Message[capacity];
	}

	public Message read()
	{
		synchronized (this)
		{
			try
			{
				while (in == out) // Queue empty.
				{
					this.wait();
				}
			}
			catch (InterruptedException ie)
			{
				throw new RuntimeException(ie);
			}

			Message msg = buffer[out].copy();

			out = (out + 1) % capacity; // getChannelCapacity()

			this.notify();

			return msg;
		}
	}

	public void write(Message msg)
	{
		synchronized (this)
		{
			try
			{
				while ((in + 1) % capacity == out) // Queue full.
				{
					this.wait();
				}
			}
			catch (InterruptedException ie)
			{
				throw new RuntimeException(ie);
			}

			buffer[in] = msg;

			in = (in + 1) % capacity;

			this.notify();
		}
	}
}
