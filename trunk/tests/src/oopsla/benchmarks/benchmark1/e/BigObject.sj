//$ bin/sessionjc tests/src/benchmarks/benchmark1/e/BigObject.sj -d tests/classes/

package benchmarks.benchmark1.e;

import java.io.Serializable;

public class BigObject implements Serializable
{
	private int id;
	private int size;

	//private int foo;

	public byte[] bytes;

	public BigObject(int id, int size)
	{
		this.id = id;
		
		if (size <= 93) // Constants determined using SerializedObjectSize.
		{
			this.size = 93;			
		}
		else if (size <= 115)
		{
			this.size = 115;			
			this.bytes = new byte[0];
		}
		else
		{
			this.size = size;
			this.bytes = new byte[size - 115];
		}
	}
	
	public void inc()
	{
		//if (this.bytes != null && this.bytes.length > 0)
		{
			this.bytes[0]++;
		}
	}
	
	public int id()
	{
		return id;
	}
	
	public int size()
	{
		return size;
	}
	
	public void print()
	{
		System.out.print(this.toString());
	}
	
	public void println()
	{
		print();
		
		System.out.println();
	}
	
	public String toString()
	{
		return "BigObject<id=" + id() + ", size=" + size() + ">";
	}		
}
