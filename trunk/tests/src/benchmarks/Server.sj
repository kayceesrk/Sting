//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/Server.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.Server 8888
/**
 * 
 * @author Raymond, Andi
 *
 */
package benchmarks;

import java.io.*;
import java.net.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import benchmarks.BigObject;

public class Server 
{
	private static final int[] msgSizes = { 1000 };
	private static final int[] sessLens = { 1000 };

	private final noalias protocol p 
	{
		sbegin
		.?[
			!<BigObject>
			//!<int>
		]*
	}		

	private ServerSocket dummy = null;
	
	public Server(int port) throws Exception 
	{					
		this.dummy = new ServerSocket(port + 1);
		
		final noalias SJServerSocket ss;
		
		try (ss)
		{
			ss = SJServerSocketImpl.create(p, port);
			
			int run = 0;
			int id = 0;		
	
			while (true) 
			{
				System.out.println("Run: " + run++);
					
				for (int i = 0; i < msgSizes.length; i++)
				{			
					for (int j = 0; j < sessLens.length; j++)
					{				
						//dummyrun(port);			
						
						final noalias SJSocket ds, s;
						
						try (ds) 
						{	
							ds = ss.accept();					
							
							ds.inwhile() // Dummy run.
							{
								BigObject bo = new BigObject(-1, msgSizes[i] - 86);
								
								ds.send(bo);
								//ds.send(123);
							}
						}
						finally
						{
							
						}
						
						try (s)
						{
							s = ss.accept(); // Actual run.
							
							s.inwhile() 
							{
								/*noalias*/ BigObject bo = new BigObject(id++, msgSizes[i] - 86);
								
								s.send(bo);
								//s.send(id++);
							}
						}
						finally 
						{ 
	
						}
						
						//System.out.println("Served: size=" + msgSizes[i] + ", length=" + sessLens[j] + ".");					
					}
				}
			}
		}
		finally
		{
			
		}
	}

	public static void main(String[] args) throws Exception 
	{
		new Server(Integer.parseInt(args[0]));
	}
	
	/*void dummyrun(int port) throws Exception
	{				
		SJServerAddress c = SJServerAddress.create(p, "foo", 1234);
		SJFSocket bar = SJFSocket.create(c);
			
		Socket s = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
	
		try 
		{					
			s = dummy.accept();						

			s.setTcpNoDelay(true);		
			
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());			

			oos.writeObject(p.encodedSJType());
		
			SJType theirs = bar.decodeSJType((String) ois.readObject()); 
	
			if (!bar.protocolType().treeDualtype(theirs))
			{
				throw new IOException("Foo: " + theirs);
			}			
			
			while (ois.readBoolean()) 
			{			
				oos.writeObject(new BigObject(-1, 1000));						
				oos.flush();
			}
		}
		finally 
		{
			try
			{	
				if (oos != null) 
				{				
					oos.flush();						
					oos.close();				
				}	
				if (ois != null) ois.close();
				if (s != null) s.close();
			}			
			catch (IOException ioe) { }			
		}			
	}*/	
}
