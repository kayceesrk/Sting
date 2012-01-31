//$ javac -cp tests/classes/ tests/src/qe/channel/QECellQueue.java -d tests/classes/

package qe.channel;

import java.util.concurrent.Semaphore;

public class QECellQueue extends BoundedChannel
{
	private QECell[] buffer;

	private int in = 0; // Next position to write (into the queue).
	private int out = 0; // Next position to read (out from the queue).

	public QECellQueue(int capacity)
	{
		super(capacity);

		buffer = new QECell[capacity];

		for (int i = 0; i < capacity; i++)
		{
			buffer[i] = new QECell();
		}
	}

	public Message read()
	{
		Message msg = buffer[out].read().copy();

		out = (out + 1) % capacity;

		return msg;
	}

	public void write(Message msg)
	{
		buffer[in].write(msg);

		in = (in + 1) % capacity;
	}
}
