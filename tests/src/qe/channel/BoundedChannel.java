//$ javac -cp tests/classes/ tests/src/qe/channel/BoundedChannel.java -d tests/classes/

package qe.channel;

abstract public class BoundedChannel extends Channel
{
	protected int capacity;

	public BoundedChannel(int capacity)
	{
		this.capacity = capacity;
	}

	public int getChannelCapacity()
	{
		return capacity;
	}
}
