//$ bin/sessionjc -cp tests/classes/ tests/src/sjservlet/client/HTTPClient.sj -d tests/classes/
//$ bin/sessionj -cp tests/classes/ sjservlet.client.HTTPClient darling.doc.ic.ac.uk 8080 'HOST=darling.doc.ic.ac.uk&PORT=8888'

package sjservlet.client;

import java.io.*;
import java.net.*;

class HTTPClient
{
	public static final String MYSERVLET_PATH = "/sjservlet"; // Tomcat servlet path.
	public static final String MYSERVLET_URL = "servlet"; // Servlet mapping URL pattern set in web.xml.

	private String host;
	private int port;

	public HTTPClient(String host, int port)
	{
		this.host = host;
		this.port = port;
	}

	public static void main(String[] args) throws Exception
	{
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String params = args[2];

		new HTTPClient(host, port).run(params);
	}

	private void run(String params) throws Exception
	{
		Socket s = null;

		//ObjectInputStream ois = null;
		//ObjectOutputStream oos = null;

		BufferedWriter bw = null;
		BufferedReader br = null;

		try
		{
			s = new Socket(host, port);

			//oos = new ObjectOutputStream(s.getOutputStream());
			//ois = new ObjectInputStream(s.getInputStream());

			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));

			writeGet(bw, params);
			readResponse(br);

			System.out.println();

			writePost(bw, params);
			readResponse(br);
		}
		finally
		{
			//oos.flush();
			//oos.close();

			//ois.close();

			if (bw != null)
			{
				bw.flush();
				bw.close();
			}
			
			if (br != null)
			{
				br.close();
			}
	
			if (s != null)
			{
				s.close();
			}
		}
	}

	private void writeGet(BufferedWriter bw, String params) throws Exception
	{
		String m;

		m = "GET " + MYSERVLET_PATH + "/" + MYSERVLET_URL + "?" + params + " HTTP/1.1\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		m = "host: " + host + ":" + port + "\n\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		bw.flush();
	}

	private void writePost(BufferedWriter bw, String params) throws Exception
	{
		String m;

		m = "POST " + MYSERVLET_PATH + "/" + MYSERVLET_URL + " HTTP/1.1\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		m = "host: " + host + ":" + port + "\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		m = "user-agent: net.clientservlet.client.HTTPClient\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		m = "content-type: application/x-www-form-urlencoded\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		//String n = "foo=1234";
		String n = params;

		m = "content-length: " + n.length() + "\n";

		System.out.print("Writing: " + m);
		bw.write(m);

		m = n;

		System.out.print("Writing: " + m);
		bw.write("\n" + m);

		bw.flush();

		System.out.println("\n");
	}

	private void readResponse(BufferedReader br) throws IOException
	{
		String m;

		for (boolean done = false; !done; )
		{
			m = br.readLine();

			System.out.println("Read: " + m);

			if (m.contains("</html>"))
			{
				done = true;
			}
		}
	}
}
