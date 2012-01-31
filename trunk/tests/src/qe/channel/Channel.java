//$ javac -cp tests/classes/ tests/src/qe/channel/Channel.java -d tests/classes/

package qe.channel;

abstract public class Channel
{
	public Channel()
	{

	}

	abstract public Message read(); // By default, should copy the message (done in read to promote asynchrony?).
	abstract public void write(Message msg);
}
