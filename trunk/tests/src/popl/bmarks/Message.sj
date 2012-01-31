//$ bin/sessionjc tests/src/popl/bmarks/Message.sj -d tests/classes/

package popl.bmarks;

import java.io.*;

public class Message implements Serializable
{
	private final byte[] payload;	
	private int count = 0;
	
	public Message(int size)
	{
		this.payload = new byte[size];
	}

	public Message(int size, int count)
	{
		this.payload = new byte[size];
		this.count = count;
	}
	
	public void inc()
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
	
	public void println()
	{
		System.out.println(this.toString());
	}
	
	public String toString()
	{
		return "Message[" + payload.length + "]:" + count;
	}
}
