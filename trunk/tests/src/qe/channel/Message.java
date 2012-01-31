//$ javac -cp tests/classes/ tests/src/qe/channel/Message.java -d tests/classes/

package qe.channel;

import java.io.*;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;

	private final byte[] payload;
	private int count = 0;

	public Message(int size)
	{
		this.payload = new byte[size];
	}

	public Message(int count, int size)
	{
		this.count = count;
		this.payload = new byte[size];
	}

	public void increment()
	{
		this.count++;
	}

	public int getCount()
	{
		return this.count;
	}

	public int getSize()
	{
		return this.payload.length;
	}

	public Message copy()
	{
		return new Message(getCount(), getSize());
	}

	public void println()
	{
		System.out.println(this.toString());
	}

	public String toString()
	{
		return "Message[" + payload.length + "]:" + count;
	}
}
