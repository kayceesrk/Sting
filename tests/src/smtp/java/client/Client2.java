//$ javac -cp tests/classes/ tests/src/smtp/java/client/Client2.java -d tests/classes/
//$ java -cp tests/classes/ smtp.java.client.Client2 smtp.cc.ic.ac.uk 25

package smtp.java.client;

import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

// Uses CharEncoder/Decoder (although could use read/writeUTF for convenience).
public class Client2
{
	public static void main(String[] args) throws Exception
	{
		Charset cs = Charset.forName("UTF8");

		System.out.println("Using charset: " + cs.name());

		CharsetEncoder ce = cs.newEncoder();
		CharsetDecoder cd = cs.newDecoder();

		String host = args[0];
		int port = Integer.parseInt(args[1]);

		Socket s = null;

		DataOutputStream dos = null;
		DataInputStream dis = null;

		try
		{
			s = new Socket(host, port);

			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());

			//final String fqdn = InetAddress.getLocalHost().getHostName().toString(); //getCanonicalHostName().toString();
			final String fqdn = "HZHL2";
			
			readMessageUntil(dis, cd, "\n");
			
			writeMessage(dos, ce, "HELO " + fqdn + "\n");
			readMessageUntil(dis, cd, "\n");
			
			//writeMessage(dos, ce, "MAIL FROM:<rhu@doc.ic.ac.uk>\n");
			writeMessage(dos, ce, "HELO " + fqdn + "\n");
			readMessageUntil(dis, cd, "\n");
			
			writeMessage(dos, ce, "RCPT TO:<ray.zh.hu@gmail.com>\n");
			readMessageUntil(dis, cd, "\n");
			
			writeMessage(dos, ce, "DATA\n");
			readMessageUntil(dis, cd, "\n");
			
			writeMessage(dos, ce, "test\n.\n");
			readMessageUntil(dis, cd, "\n");
			
			writeMessage(dos, ce, "QUIT\n");
			readMessageUntil(dis, cd, "\n");			

			for (int x = dis.read(); x != -1; x = dis.read())
			{
				System.out.print((char) x);
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
	
	private static void writeMessage(DataOutputStream dos, CharsetEncoder ce, String msg) throws IOException
	{
		Charset cs = Charset.forName("UTF8");
		CharsetEncoder ce1 = cs.newEncoder();
		
		System.out.print("Sending: " + msg + ", " + msg.length());
		
		//System.out.print("Sending: " + msg);
		
		//ce.reset();
		
		byte[] bs = ce1.encode(CharBuffer.wrap(msg)).array();
		
		System.out.println("\n1: " + Arrays.toString(bs) + ", " + bs.length);
		
		//dos.write(bs, 0, bs.length - 1);		
		dos.write(bs);
		
		dos.flush();
	}	
	
	private static void readMessageUntil(DataInputStream dis, CharsetDecoder cd, String s) throws IOException
	{
		System.out.print("Received: ");
		
		ByteBuffer bb = ByteBuffer.allocate(1024);
		
		String m = null;
		
		for (m = cd.decode(getFlippedReadOnlyByteBuffer(bb)).toString(); !m.endsWith(s); m = cd.decode(getFlippedReadOnlyByteBuffer(bb)).toString())
		{
			bb.put(dis.readByte());
		}
		
		System.out.print(m);
	}
	
	private static final ByteBuffer getFlippedReadOnlyByteBuffer(ByteBuffer bb)
	{
		ByteBuffer fbb = bb.asReadOnlyBuffer();
		
		fbb.flip();
		
		return fbb;
	}	
}
