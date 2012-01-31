//$ bin/sessionjc -cp tests/classes/ tests/src/places/pi2/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ places.pi2.LocalRun 2 

package places.pi2;

import java.util.*;

import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

/**
 * @author Raymond
 *
 */
public class LocalRun
{
	private static final int port_m = 4439;
	private static final int port_w = 4440;
	
	public static void main(String[] args) throws Exception
	{
		configureTransports();
		
		final int numWorkers = Integer.parseInt(args[0]);				

		for (int i = 0; i < numWorkers; i++)
		{
			final int j = i;
			
			new Thread()
			{
				public void run()
				{
					try
					{
						Worker.main(new String[] { new Integer(port_w + j).toString() } );
					}
					catch (Exception x)
					{
						throw new RuntimeException(x);
					}					
				}
			}.start();
		}
		
		new Thread()
		{
			public void run()
			{
				String[] ms = new String[1 + numWorkers * 2];
				
				ms[0] = new Integer(port_m).toString();
				
				for (int i = 0; i < numWorkers; i++)
				{
					ms[i * 2 + 1] = "localhost";
					ms[i * 2 + 2] = new Integer(port_w + i).toString();
				}
				
				try
				{
					Master.main(ms);
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}				
			}
		}.start();		
	}
	
	private static void configureTransports()
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		//ts.add(new SJFifoPair());
		ss.add(new SJStreamTCP());
		
		//ts.add(new SJFifoPair());
		ts.add(new SJStreamTCP());
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}	
}
