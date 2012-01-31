//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/NewChannel.sj -d tests/classes/

package popl.bmarks.bmark2;

import java.util.*;

import popl.bmarks.*;

class NewCell implements Cell
{
	public volatile boolean readFlag = false;
	public volatile boolean writeFlag = false;
	//public Message message = null;
	public volatile Message message = null;
	public final Object lock = new Object();
}

public class NewChannel implements SpinChannel
{
	private final int size;
	
	private final NewCell[] cells;
	
	private int read = 0;
	private int write = 0;
	
	private int spins = 0;
	private int nospins = 0;
	
	public NewChannel(int size)
	{
		this.size = size;
		
		cells = new NewCell[size];
		
		for (int i = 0; i < size; i++)
		{
			cells[i] = new NewCell();
		}
	}
	
	public Message read()
	{
		NewCell cell = cells[read];
		
		cell.readFlag = true;
		
		Message msg;
		
		/*if (cell.writeFlag)
		{
			msg = cell.message;
			
			cell.readFlag = false;
		}
		else
		{
			synchronized (cell.lock)
			{
				cell.lock.wait();
			}
			
			msg = cell.message;
			
			cell.readFlag = false;
		}*/
		
		if (!cell.writeFlag)
		{
			try
			{
				synchronized (cell.lock)
				{													
					cell.lock.wait();
				}
			}
			catch (InterruptedException ie)
			{
				throw new RuntimeException(ie);
			}			
		}
		
		cell.readFlag = false;
		
		msg = cell.message;
		
		read++;	
			
		return msg;
	}
	
	public void write(Message msg)
	{
		NewCell cell = cells[write];
		
		cell.message = msg;
		
		cell.writeFlag = true;
		
		if (!cell.readFlag)
		{
			nospins++;
		}
		
		//if (cell.readFlag)
		{
			while (cell.readFlag) 
			{
				spins++;
				
				synchronized (cell.lock)
				{					
					cell.lock.notify();
					
					//Thread.yield();
				}
			}
		}
		
		write++;
	}
	
	public int getSpins()
	{
		return spins;
	}
	
	public int getNospins()
	{
		return nospins;
	}	
}
