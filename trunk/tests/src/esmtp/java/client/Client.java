//$ javac -cp tests/classes/ tests/src/esmtp/java/client/Client.java -d tests/classes/
//$ java -cp tests/classes/ esmtp.java.client.Client esmtp.cc.ic.ac.uk 25

package esmtp.java.client;

import java.io.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

// Uses BufferedReader/Writer.
public class Client
{
	public static void main(String[] args) throws Exception
	{
		Charset cs = Charset.forName("UTF8");

		System.out.println("Using charset: " + cs.name());

		String host = args[0];
		int port = Integer.parseInt(args[1]);

		//String name = args[2];

		Socket s = null;

		BufferedWriter bw = null;
		final BufferedReader br;

		//Scanner sc = new Scanner(System.in);

		try
		{
			s = new Socket(host, port);

			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), cs));
			br = new BufferedReader(new InputStreamReader(s.getInputStream(), cs));

			/*new Thread() // Doesn't seem to work: maybe the SMTP server doesn't like receiving messages asynchronously.
			{
				public void run()
				{
					try
					{
						for (int x = br.read(); x != -1; x = br.read())
						{
							System.out.print((char) x);
						}
					}
					catch (IOException ioe)
					{
						ioe.printStackTrace();
					}
				}
			}.start();*/

			final String fqdn = InetAddress.getLocalHost().getHostName().toString(); //getCanonicalHostName().toString();

			readMessageUntilChar(br, (int) '\n');

			writeMessage(bw, "HELO " + fqdn + "\n");
			readMessageUntilChar(br, (int) '\n');

			writeMessage(bw, "MAIL FROM:<rhu@doc.ic.ac.uk>\n");
			readMessageUntilChar(br, (int) '\n');

			writeMessage(bw, "RCPT TO:<ray.zh.hu@gmail.com>\n");
			readMessageUntilChar(br, (int) '\n');

			writeMessage(bw, "DATA\n");
			readMessageUntilChar(br, (int) '\n');

			writeMessage(bw, "test\n.\n");
			readMessageUntilChar(br, (int) '\n');

			writeMessage(bw, "QUIT\n");
			readMessageUntilChar(br, -1); // No need to read until EOF; can be '\n' as above.
		}
		finally
		{
			bw.flush();
			bw.close();

			//br.close();

			s.close();
		}
	}

	private static void writeMessage(BufferedWriter bw, String msg) throws IOException
	{
		System.out.print("Sending: " + msg);

		bw.write(msg);
		bw.flush();
	}

	private static void readMessageUntilChar(BufferedReader br, int j) throws IOException
	{
		System.out.print("Received: ");

		for (int i = br.read(); i != j; i = br.read())
		{
			System.out.print((char) i);
		}

		if (j != -1)
		{
			System.out.print((char) j);
		}
	}

	/*private static void readMessageUntilEOF(BufferedReader br) throws IOException
	{
		for (int i = br.read(); i != -1; i = br.read())
		{
			System.out.print((char) i);
		}
	}*/
}
