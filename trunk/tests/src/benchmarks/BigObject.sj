//$ bin/sessionjc tests/src/benchmarks/BigObject.sj -d tests/classes/
/**
 * 
 * @author Raymond, Andi
 *
 */
package benchmarks;

import java.io.Serializable;

public class BigObject implements Serializable
{
	private int id;
	private int size;

	public byte[] bytes;

	public BigObject(int id, int size)
	{
		this.id = id;
		this.size = size;

		bytes = new byte[size];
	}

	public String toString()
	{
		return "BigObject<id=" + id + ", size=" + size + ">";
	}
	
	public int id()
	{
		return id;
	}
	
	public int size()
	{
		return size;
	}
}
