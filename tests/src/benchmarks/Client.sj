//$ bin/sessionjc -cp tests/classes/ tests/src/benchmarks/Client.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ benchmarks.Client localhost 8888 1000 1000
//$ tests/src/benchmarks/client.sh localhost 8888 100

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

public class Client 
{
	private final noalias protocol p 
	{
		cbegin
		.![ 
			?(BigObject)
			//?(int)
		]*
	}	
	
	public Client(String server, int port, int msgSize, int sessLen) throws Exception 
	{	
		final noalias SJService c = SJService.create(p, server, port);
		
		final noalias SJSocket ds, s;			
			
		//dummyrun(server, port);
			
		long timeStarted = -1;		
		long timeFinished = -1;
		
		try (ds) 
		{
			ds = c.request(); // Dummy run.							
			
			//System.out.println("Client connection established");
			
			int k = 0;

			ds.outwhile(k++ < 1) 
			{
				//System.out.println("Client Receiving...");
				
				BigObject bo = (BigObject) ds.receive();			
				//int v = ds.receiveInt();
			}									
		}
		finally
		{
			
		}
		
		try (s)
		{	
			s = c.request(); // Actual run.					
			
			timeStarted = System.nanoTime();
										
			int k = 0;

			s.outwhile(k++ < sessLen) 
			{
				BigObject bo = (BigObject) s.receive();
				//int v = s.receiveInt();
			 
				//System.out.println("Received: " + bo);	
			}
						
			timeFinished = System.nanoTime();										
		}
		finally 
		{

		}									

		//System.out.println("Object size " + msgSize + ", session length " + sessLen + ": " + Math.round(((float) (timeFinished - timeStarted)) / 1000.0) + "ms.");				
		System.out.println(Math.round(((float) (timeFinished - timeStarted)) / 1000.0)); // Microseconds. 
	}

	public static void main(String args[]) throws Exception 
	{
		new Client(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
	}

	/*void dummyrun(String server, int port) throws Exception
	{		
		SJServerAddress c = SJServerAddress.create(p, server, port);
		SJFSocket bar = SJFSocket.create(c);
		
		Socket s = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;	
	
		try 
		{					
			InetSocketAddress addr = new InetSocketAddress(server, port + 1);
		
			s = new Socket(addr.getAddress(), addr.getPort());		
		
			s.setTcpNoDelay(true);
			
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());

			oos.writeObject(p.encodedSJType());
		
			SJType theirs = bar.decodeSJType((String) ois.readObject()); 
	
			if (!bar.protocolType().treeDualtype(theirs))
			{
				throw new IOException("Foo: " + theirs);
			}
			
			for (int k = 0; k < 1; k++)
			{
				oos.writeBoolean(true);		
				oos.flush();
								
				BigObject bo = (BigObject) ois.readObject();					
			}

			oos.writeBoolean(false);
			oos.flush();
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
