//$ bin/sessionjc -cp tests/classes/ tests/src/onedimjacobi/noaliaz/LocalRun.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ onedimjacobi.noaliaz.LocalRun fs

package onedimjacobi.noaliaz;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.transport.*;
import sessionj.runtime.transport.tcp.*;
import sessionj.runtime.transport.sharedmem.*;

public class LocalRun
{
	private static void configureTransports(String transports)
	{
		List ss = new LinkedList();
		List ts = new LinkedList();
		
		if (transports.contains("f"))
		{
			ss.add(new SJFifoPair());
			ts.add(new SJFifoPair());
		}
		
		if (transports.contains("s"))
		{
			ss.add(new SJStreamTCP());
			ts.add(new SJStreamTCP());
		}
				
		if (transports.contains("m"))
		{
			ss.add(new SJManualTCP());
			ts.add(new SJManualTCP());
		}		
				
		SJTransportManager sjtm = SJRuntime.getTransportManager();
		
		sjtm.configureSetups(ss);
		sjtm.configureTransports(ts);				
	}
	
	public static void main(final String[] args) throws Exception
	{	
		String transports = args[0];
	
		configureTransports(transports);
	
		new Thread()
		{
			public void run()
			{
				try
				{
					WorkerN.main(new String[] { "4442" });
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
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
					Master.main(new String[] { "4444", "localhost", "4442", "localhost", "4443" });
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
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
					WorkerS.main(new String[] { "4443" });
				}
				catch (Exception x)
				{
					throw new RuntimeException(x);
				}				
			}
		}.start();					
	}
}
