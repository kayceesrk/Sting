//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/OrdinaryChannel.sj -d tests/classes/

package popl.bmarks.bmark2;

import java.util.*;

import popl.bmarks.*;

public class OrdinaryChannel implements Channel
{
	private final List chan = new LinkedList();
	
	public OrdinaryChannel()
	{
		
	}
	
	public synchronized Message read()
	{
		try
		{
			while (chan.isEmpty())
			{
				this.wait();
			}
		}
		catch (InterruptedException ie)
		{
			throw new RuntimeException(ie);
		}
		
		return (Message) chan.remove(0);
	}
	
	public synchronized void write(Message msg)
	{
		chan.add(msg);
		
		this.notifyAll();
	}
}
