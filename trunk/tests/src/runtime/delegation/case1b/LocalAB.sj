//$ bin/sessionjc -cp tests/classes tests/src/runtime/delegation/case1b/LocalAB.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ runtime.delegation.case1b.LocalAB 8888 9999

package runtime.delegation.case1b;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;


public class LocalAB
{		
	private static void configureTransports()
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		ss.add(new SJFifoPair());
		ss.add(new SJStreamTCP());
		//ss.add(new SJManualTCP());				

		ts.add(new SJFifoPair());
		ts.add(new SJStreamTCP());
		//ts.add(new SJManualTCP());
		
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}	
	
	public static void main(final String[] args) throws Exception
	{
		configureTransports();
		
		final String portb = args[0];
		final String portc = args[1];
		
		new Thread()
		{
			public void run()
			{
				try
				{
					B.main(new String[] { portb, "localhost", portc });
				}
				catch (Exception x)
				{
					x.printStackTrace();
				}				
			}
		}.start();
		
		Thread.sleep(500);		
		
		new Thread()
		{
			public void run()
			{
				try
				{
					A.main(new String[] { "localhost", portb });
				}
				catch (Exception x)
				{
					x.printStackTrace();
				}				
			}
		}.start();		
	}
}
