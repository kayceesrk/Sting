//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/BusyWaitChannel.sj -d tests/classes/

package popl.bmarks.bmark2;

import java.util.*;

import popl.bmarks.*;

class BusyWaitCell implements Cell
{
	public volatile boolean writeFlag = false;
	public Message message = null;
}

public class BusyWaitChannel implements SpinChannel
{
	private final int size;
	
	private final BusyWaitCell[] cells;
	
	private int read = 0;
	private int write = 0;
	
	private int spins = 0;
	private int nospins = 0;
	
	public BusyWaitChannel(int size)
	{
		this.size = size;
		
		cells = new BusyWaitCell[size];
		
		for (int i = 0; i < size; i++)
		{
			cells[i] = new BusyWaitCell();
		}
	}
	
	public Message read()
	{
		BusyWaitCell cell = cells[read];
		
		Message msg;
				
		if (cell.writeFlag)
		{
			nospins++;
		}
		
		while (!cell.writeFlag)
		{
			spins++;
		}
		
		msg = cell.message;
		
		read++;	
			
		return msg;
	}
	
	public void write(Message msg)
	{
		BusyWaitCell cell = cells[write];
		
		cell.message = msg;
		
		cell.writeFlag = true;
		
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
