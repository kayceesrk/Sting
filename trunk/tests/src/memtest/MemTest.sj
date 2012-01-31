//$ bin/sessionjc -cp tests/classes tests/src/memtest/MemTest.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ memtest.MemTest 8888

package memtest;

import java.util.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;

import memtest.server.*;
import memtest.client.*;

public class MemTest
{		
	public static void main(String[] args) throws Exception
	{						
		final int port = Integer.parseInt(args[0]);
		
		new Thread()
		{
			public void run()
			{
				try
				{
					new Server(port);
				}
				catch (Exception x)
				{
					//System.out.println("Server: " + x);
					throw new RuntimeException(x);
				}
			}
		}.start();
		
		Thread.sleep(1000);
		
		new Thread()
		{
			public void run()
			{
				try
				{
					new Client("localhost", port);
				}
				catch (Exception x)
				{
					//System.out.println("Client: " + x);
					throw new RuntimeException(x);
				}				
			}
		}.start();		
	}
}
