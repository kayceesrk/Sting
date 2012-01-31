//$ javac -cp classes/ tests/src/esmtp/java/server/Server.java -d tests/classes/
//$ java -cp tests/classes/ esmtp.java.server.Server 8888

package esmtp.java.server;

import java.io.*;
import java.nio.charset.*;
import java.net.*;
import java.util.*;

// Uses BufferedReader/Writer.
public class Server
{
	public static void main(String[] args) throws Exception
	{
		Charset cs = Charset.forName("UTF8");

		System.out.println("Using charset: " + cs.name());

		int port = Integer.parseInt(args[0]);

		ServerSocket ss = null;

		Socket s = null;

		BufferedReader br = null;
		BufferedWriter bw = null;

		try
		{
			ss = new ServerSocket(port);

			//Scanner sc = new Scanner(System.in);

			s = ss.accept();

			br = new BufferedReader(new InputStreamReader(s.getInputStream(), cs));
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), cs));

			//String msg = "GET " + name + "\n\n";
			String msg = "3A";

			System.out.println("Sending: " + msg);

			bw.write(msg);
			bw.flush();

			/*for (int x = br.read(); x != -1; x = br.read())
			{
				System.out.print((char) x);
			}*/
		}
		finally
		{
			ss.close();

			bw.flush();
			bw.close();

			br.close();

			s.close();
		}
	}
}
