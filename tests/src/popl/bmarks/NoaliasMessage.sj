//$ bin/sessionjc tests/src/popl/bmarks/NoaliasMessage.sj -d tests/classes/

package popl.bmarks;

import java.io.*;

public class NoaliasMessage implements Serializable
{
	private final noalias byte[] payload;
	
	private int count = 0;
	
	public NoaliasMessage(int size)
	{
		this.payload = new byte[size];
	}
	
	public void inc()
	{
		this.count++;
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
