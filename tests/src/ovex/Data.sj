//$ bin/sessionjc tests/src/ovex/Data.sj -d tests/classes/

package ovex;

import java.io.*;

public class Data implements Serializable
{
	private int value;
	private noalias Data next;
	
	public Data(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public void setValue(int value)
	{
		this.value = value;
	}
	
	public noalias Data tail()
	{
		return getNext();
	}
	
	public noalias Data getNext()
	{
		return next;
	}
	
	public void setNext(noalias Data next)
	{
		this.next = next;
	}
	
	public String toString()
	{
		noalias Data foo = getNext();
		
		String m = new Integer(value).toString(); 
		
		if (foo == null)
		{
			return m;
		}
		
		m += ", " + foo.toString();
		
		setNext(foo);
		
		return m;
	}
}
