//$ javac -cp tests/classes/ tests/src/qe/channel/BlockingQueue.java -d tests/classes/

package qe.channel;

import java.util.concurrent.ArrayBlockingQueue;

public class BlockingQueue extends BoundedChannel
{
	private ArrayBlockingQueue<Message> queue;

	public BlockingQueue(int capacity)
	{
		super(capacity);

		queue = new ArrayBlockingQueue<Message>(capacity);
	}

	public Message read()
	{
		try
		{
			return queue.take();
		}
		catch (InterruptedException ie)
		{
			throw new RuntimeException(ie);
		}
	}

	public void write(Message msg)
	{
		queue.add(msg);
	}
}
