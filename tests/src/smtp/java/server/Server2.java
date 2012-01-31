//$ javac -cp classes/ tests/src/smtp/java/server/Server2.java -d tests/classes/
//$ java -cp tests/classes/ smtp.java.server.Server2 8888

package smtp.java.server;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

// Uses CharEncoder/Decoder.
public class Server2
{
	public static void main(String[] args) throws Exception
	{
		Charset cs = Charset.forName("UTF8");

		System.out.println("Using charset: " + cs.name());

		CharsetEncoder ce = cs.newEncoder();
		CharsetDecoder cd = cs.newDecoder();

		int port = Integer.parseInt(args[0]);

		ServerSocket ss = null;

		Socket s = null;

		DataOutputStream dos = null;
		DataInputStream dis = null;

		try
		{
			ss = new ServerSocket(port);

			//Scanner sc = new Scanner(System.in);

			s = ss.accept();

			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());

			//String msg = "GET " + name + "\n\n";
			String msg = "3A";

			System.out.println("Sending: " + msg);

			//ce.reset();

			CharBuffer cb = CharBuffer.wrap(msg);
			ByteBuffer bb = ce.encode(cb);

			dos.write(bb.array());
			dos.flush();

			/*for (int x = br.read(); x != -1; x = br.read())
			{
				System.out.print((char) x);
			}*/
		}
		finally
		{
			ss.close();

			dos.flush();
			dos.close();

			dis.close();

			s.close();
		}
	}
}
