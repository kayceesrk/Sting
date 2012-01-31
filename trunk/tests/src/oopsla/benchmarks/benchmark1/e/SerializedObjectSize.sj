//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/benchmark1/e/SerializedObjectSize.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.benchmark1.e.SerializedObjectSize 1000

package benchmarks.benchmark1.e;

import java.io.*;
//import java.net.*;

public class SerializedObjectSize
{
	public static void main(String[] args) throws Exception
	{
		int size = Integer.parseInt(args[0]);
	
		BigObject bo = new BigObject(-1, size);
		
		bo.println();
	
		byte[] bobytes = serializeObject(bo);
	
		System.out.println("Serialized object (" + size + "): " + bobytes.length);
	}

	public static byte[] serializeObject(Object o) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); // Could optimise by reusing the byte array.
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		oos.writeObject(o);

		return baos.toByteArray();
	}
}
