//$ bin/sessionj -cp tests/classes ecoop.bmarks2.macro.smtp.client.DummyClient false localhost 2525 1024

package ecoop.bmarks2.macro.smtp.client;

import java.net.*;
import java.io.*;
import java.util.*;

import ecoop.bmarks2.micro.Common;

// Not too far away from the micro benchmark LoadClient functionality.
public class DummyClient
{			
	private static String text;
	
	private boolean debug = false;
	
	public DummyClient(boolean debug)
	{
		this.debug = debug;
		
		StringBuilder sb = new StringBuilder(10240);
		
		for (int i = 0; i < sb.capacity(); i++)
		{
			sb.append('a');
		}
		
		DummyClient.text = sb.toString();
	}
	
	public void run(String server, int port, int msgSize, boolean[] ack, boolean[] spin) throws Exception
	{
		final String fqdn = InetAddress.getLocalHost().getHostName().toString(); //getCanonicalHostName().toString();
		
		debugPrintln("fqdn: " + fqdn);
		
		Socket s = null;
		
		DataOutputStream dos = null;
		DataInputStream dis = null;
		
		try
		{		
			debugPrintln("Requesting SMTP session with: " + server + ":" + port);
			
			s = new Socket(server, port);
							
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
						
			read(dis, "Server greeting: ");
			
			synchronized (ack) // Duplicated from LoadClient.
	  	{
	  		ack[0] = true;
	  		
	  		ack.notify();
	  	}			
			
			write(dos, "EHLO " + fqdn + "\r\n");			
			read(dis, "EHLO ack: ");
			
			for (boolean run = true; run; )			
			{
				write(dos, "MAIL FROM:<foo@bar.com>\r\n");
				
				String msg = read(dis, "MAIL ack: ");
				
				if (msg.contains("QUIT")) // HACK.
				{
					write(dos, "QUIT\r\n");
					
					run = false;
				}
				else
				{				
					write(dos, "RCPT TO:<quux@bar.com>\r\n");            
					read(dis, "RCPT ack: ");			 
					write(dos, "DATA\r\n");			 
					read(dis, "DATA ack: ");			 
					write(dos, generateText(msgSize) + "\r\n.\r\n");			 
					read(dis, "Message data ack: ");
				}
       
				if (debug)
				{
					Thread.sleep(1000);
				}
				
				synchronized (spin) {
					while (!spin[0]) {
						spin.wait();
					}
				}
			}		
			
			Thread.sleep(50);
		}
		finally
		{
			Common.closeOutputStream(dos);
			Common.closeInputStream(dis);
			Common.closeSocket(s);		
		}
	}
    
	private final void write(DataOutputStream dos, String m) throws IOException
	{
		debugPrintln("Sending: " + m);
		dos.write(m.getBytes());
		dos.flush();
	}
	
	private final String read(DataInputStream dis, String prefix) throws IOException
	{
		String m = dis.readLine();
		
		debugPrintln(prefix + m);
		
		return m;
	}
	
	private static String generateText(int msgSize)
	{
		if (msgSize > text.length()) 
		{
			throw new RuntimeException("[DummyClient] Message size too big: " + msgSize);
		}		
		
    return text.substring(0, msgSize);
  }

	private final void debugPrintln(String m)
	{
		if (debug)
		{
			System.out.println(m);
		}
	}
	/*
	public static void main(String[] args) throws Exception
	{
		boolean debug = Boolean.parseBoolean(args[0]);
		
		String server = args[1];
		int port = Integer.parseInt(args[2]);		
		
		int msgSize = Integer.parseInt(args[3]);
		
		new DummyClient(debug).run(server, port, msgSize, new boolean[1], new boolean[1]);
	}
	*/
}
