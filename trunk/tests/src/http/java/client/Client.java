//$ javac -cp tests/classes/ tests/src/http/java/client/Client.java -d tests/classes/
//$ java -cp tests/classes/ http.java.client.Client www.google.com 80 /

package http.java.client;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

public class Client
{
	public static void main(String[] args) throws Exception
	{
		Charset cs = Charset.forName("UTF8");

		System.out.println("Using charset: " + cs.name());

		CharsetEncoder ce = cs.newEncoder();
		CharsetDecoder cd = cs.newDecoder();

		String host = args[0];
		int port = Integer.parseInt(args[1]);
		
		String path = args[2];

		Socket s = null;

		DataOutputStream dos = null;
		DataInputStream dis = null;

		try
		{
			s = new Socket(host, port);

			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());

			String msg = "GET " + path + "\n\n";

			System.out.println("Sending: " + msg);

			//ce.reset();

			CharBuffer cb = CharBuffer.wrap(msg);
			ByteBuffer bb = ce.encode(cb);

			dos.write(bb.array());
			dos.flush();

			for (int i = dis.read(); i != -1; i = dis.read())
			{
				System.out.print((char) i);
			}
		}
		finally
		{
			dos.flush();
			dos.close();

			dis.close();

			s.close();
		}
	}
}
