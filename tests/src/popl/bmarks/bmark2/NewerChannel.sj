//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/NewerChannel.sj -d tests/classes/

package popl.bmarks.bmark2;

import java.util.*;

import popl.bmarks.*;

class NewerCell implements Cell
{
	public volatile boolean readFlag = false;
	public volatile boolean writeFlag = false;
	public Message message = null;
}

public class NewerChannel implements SpinChannel
{
	private final int size;
	
	private final NewerCell[] cells;
	
	private int read = 0;
	private int write = 0;
	
	private int spins = 0;
	private int nospins = 0;
	
	public NewerChannel(int size)
	{
		this.size = size;
		
		cells = new NewerCell[size];
		
		for (int i = 0; i < size; i++)
		{
			cells[i] = new NewerCell();
		}
	}
	
	public Message read()
	{
		NewerCell cell = cells[read];
		
		Message msg;
		
		if (!cell.writeFlag)
		{
			try
			{
				synchronized (cell)
				{
					cell.readFlag = true;	// Must be inside the synchronized, or else Writer can get in and do the notify after this operation before we get to the wait. 			
					
					//while (!cell.writeFlag) // Is this necessary? Or is if enough?
					if (!cell.writeFlag)
					{
						spins++;
						
						cell.wait();
					}
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
		NewerCell cell = cells[write];
		
		cell.message = msg;
		
		cell.writeFlag = true;
		
		if (!cell.readFlag)
		{
			nospins++;
		}
		
		if (cell.readFlag)
		{
			synchronized (cell)
			{					
				cell.notify();
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
