package sessionj.runtime.transport.httpservlet;

import java.io.*;
import java.net.*;
import java.util.*;

import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.transport.*;

import static sessionj.runtime.transport.httpservlet.servlet.SJHTTPProxyServlet.*;
import static sessionj.runtime.util.SJRuntimeUtils.closeStream;

/**
 * 
 * @author Raymond
 *
 * FIXME: improve byte array encoding routines. Direct connections not yet done. Connections from failed Client-Servlet sessions not cleaned up at Servlet (Server hangs); probably need timeouts. Need to allow for "Connection: close", i.e. don't rely on persistence. Use "Connection: close" to clean up Servlet side.       
 *
 */
public class SJHTTPServletConnection extends AbstractSJConnection
{
	private static final boolean debug = false;
	//private static final boolean debug = true;
	
	public static final boolean TCP_NO_DELAY = true; // Should use a global setting.
	
	public static final int SJ_HTTP_SERVLET_PORT = 8080; // The default Tomcat port.
	
	public static final String SJ_HTTP_SERVLET_PATH = "/sjhttpproxyservlet"; // Tomcat servlet path (web application sub-directory name).
	public static final String SJ_HTTP_SERVLET_URL = "servlet"; // Servlet mapping URL pattern set in web.xml.
	
	public static final String HTTP_VERSION = "HTTP/1.1";  
	
	public static final String HTTP_HOST_HEADER = "Host";
	public static final String HTTP_USER_AGENT_HEADER = "User-Agent";
	public static final String HTTP_CONTENT_TYPE_HEADER = "Content-Type";
	public static final String HTTP_FORM_URLENCODED_MEDIA_TYPE = "application/x-www-form-urlencoded";
	public static final String HTTP_CONTENT_LENGTH_HEADER = "Content-Length";	
	
	public static final String HTTP_CONNECTION_HEADER = "Connection"; // FIXME: cannot rely on HTTP persistence. If prematurely closed (this header is sent), need to re-establish connection. And if we fail, need to include this header so Tomcat can close the connection. // FIXME: need to worry about case?
	public static final String HTTP_CONNECTION_CLOSE = "close";
	
	public static final String SJ_HTTP_SERVLET_TARGET_HOST = "thost";
	public static final String SJ_HTTP_SERVLET_TARGET_PORT = "tport";
	public static final String SJ_HTTP_SERVLET_LOCAL_HOST = "lhost"; // Redundant: can get this information from HTTP request object. 
	public static final String SJ_HTTP_SERVLET_LOCAL_PORT = "lport";
	
	public static final String SJ_HTTP_SERVLET_WRITE_BYTE = "wbyte";
	public static final String SJ_HTTP_SERVLET_WRITE_BYTE_ARRAY = "wbarray";
	public static final String SJ_HTTP_SERVLET_READ_BYTE = "rbyte";
	public static final String SJ_HTTP_SERVLET_READ_BYTE_ARRAY = "rbarray";
	public static final String SJ_HTTP_SERVLET_FIN = "FIN";
	
	private boolean usingServletProxy;
	
	private final String hostName;
	private int port;
	
	private Socket s;

	//private DataOutputStream dos; // read/writeUTF doesn't seem to work.
	//private DataInputStream dis;
	private BufferedWriter bw;
	private BufferedReader br;

	private boolean closed;
	
	private static final String localHostName; // Because getLocalAddress on s seems bugged.

    static 
	{
		try
		{
			//localHostName = InetAddress.getLocalHost().getHostName(); // Doesn't match ServletRequest.getHostName() for e.g. HZHL2 on IC-DoC. // Transport-level name, although currently should be same as network-level name (DNS and IP).
			localHostName = InetAddress.getLocalHost().getHostAddress(); // The main SJRuntime routines are also using host address.
		}
		catch (UnknownHostException uhe)
		{
			throw new SJRuntimeException(uhe);
		}
	}
	
	public SJHTTPServletConnection(String hostName, int port, SJTransport transport) throws SJIOException
	{
        super(transport);
        this.hostName = hostName;
		this.port = port;

        closed = false;
		
		try
		{
			connect(hostName, port); // FIXME: this can be slow to fail when the direct connection is not available. Any way to check availability of direct connection faster?  
					
			this.usingServletProxy = false;
			
			if (debug)
			{
				System.out.println("[SJHTTPServletConnection] Direct connection established to " + hostName + ":" + port);
			}
		}
		catch (SJIOException ioe)
		{
			this.port = port - SJHTTPServlet.TCP_PORT_MAP_ADJUST;
			
			connect(hostName, SJ_HTTP_SERVLET_PORT);
			
			this.usingServletProxy = true;
			
			if (debug)
			{
				System.out.println("[SJHTTPServletConnection] Connection established to SJHTTPProxyServlet proxy at " + hostName + ":" + SJ_HTTP_SERVLET_PORT);
			}
		}
		
		if (usingSJServletProxy())
		{
			init();
		}
	}
	
	public SJHTTPServletConnection(Socket s, SJTransport transport) throws SJIOException
	{
        super(transport);
        this.s = s;
        this.hostName = s.getInetAddress().getHostName();
		this.port = s.getPort();
		
		try
		{
			s.setTcpNoDelay(TCP_NO_DELAY);
			
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		} 
		catch (IOException ioe) 
		{
			throw new SJIOException(ioe);
		}		
	}

	private void connect(String hostName, int port) throws SJIOException
	{
		try
		{
			s = new Socket(hostName, port);
			
			s.setTcpNoDelay(TCP_NO_DELAY);
			
			bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		} 
		catch (IOException ioe) 
		{
			throw new SJIOException(ioe);
		}		
	}
	
	private void init() throws SJIOException
	{
		String params = makeTargetParameters(); // FIXME: initialisation should have an explicit header like the other signals.
		
		params += "&" + SJ_HTTP_SERVLET_LOCAL_HOST + "=" + getLocalHostName();
		params += "&" + SJ_HTTP_SERVLET_LOCAL_PORT + "=" + Integer.toString(getLocalPort());
		
		//int tport = usingSJServletProxy() ? SJ_HTTP_SERVLET_PORT : getPort();
		int tport = SJ_HTTP_SERVLET_PORT;
		
		writeGet(getHostName(), tport, params, false);
		
		String m = readResponse();
		
		if (!m.equals(CONNECTION_SUCCESS))
		{
			throw new SJIOException("[" + getTransportName() + "] Connection failed: " + m);
		}
	}
	
	public void disconnect() // throws SJIOException
	{
		if (usingSJServletProxy())
		{
			String params = makeTargetParameters(); 
				
			params +=	"&" + SJ_HTTP_SERVLET_FIN + "=true";
			
			int tport = SJ_HTTP_SERVLET_PORT;
			
			try
			{
				writeGet(getHostName(), tport, params, true);
			}
			catch (SJIOException ioe)
			{
				
			}
		}
		
		closed = true;
		
		try { bw.flush(); bw.close(); } catch (IOException ioe) { }
		try { br.close(); } catch (IOException ioe) { }
		
		try 
		{ 
			if (s != null)
			{
				s.close(); 			
			}
		}
		catch (IOException ioe) 
		{ 
			
		}		
	}
	
	public void writeByte(byte b) throws SJIOException 
	{
		String data = makeTargetParameters();
		
		data += "&" + SJ_HTTP_SERVLET_WRITE_BYTE + "=" + encodeByte(b);
		
		int tport = usingSJServletProxy() ? SJ_HTTP_SERVLET_PORT : getPort();
		
		writePost(getHostName(), tport, data);
	}
	
	public void writeBytes(byte[] bs) throws SJIOException 
	{
		String data = makeTargetParameters();
		
		data += "&" + SJ_HTTP_SERVLET_WRITE_BYTE_ARRAY + "=" + encodeByteArray(bs); // FIXME: optimise.
		
		int tport = usingSJServletProxy() ? SJ_HTTP_SERVLET_PORT : getPort();
		
		writePost(getHostName(), tport, data);
	}
	
	public byte readByte() throws SJIOException
	{	
		byte b = -1;
		
		if (usingSJServletProxy())
		{
			String params = makeTargetParameters();
			
			params += "&" + SJ_HTTP_SERVLET_READ_BYTE + "=true";
			
			writeGet(getHostName(), SJ_HTTP_SERVLET_PORT, params, false);
						
			String m = readResponse();
			
			if (m.equals(IO_ERROR))
			{
				throw new SJIOException("[" + getTransportName() + "] Servlet I/O error.");
			}
			
			b = decodeByte(m);
		}
		else
		{
			String m = parsePostedMessage(); // The POST message body, i.e. the POST parameters. 
			
			m = m.substring(m.lastIndexOf("=") + 1); // Should be "wbyte=...".
			
			b = Byte.parseByte(m);
		}
		
		return b;
	}
	
	public void readBytes(byte[] bs) throws SJIOException
	{
		String ba = null;
		
		if (usingSJServletProxy())
		{
			String params = makeTargetParameters();
			
			params += "&" + SJ_HTTP_SERVLET_READ_BYTE_ARRAY + "=" + Integer.toString(bs.length); // Could factor out encoding of integer.
			
			writeGet(getHostName(), getPort(), params, false);
						
			ba = readResponse();
			
			if (ba.equals(IO_ERROR))
			{
				throw new SJIOException("[" + getTransportName() + "] Servlet I/O error.");
			}
		}
		else
		{
			ba = parsePostedMessage(); // The POST message body, i.e. the POST parameters. 
			
			ba = ba.substring(ba.lastIndexOf("=") + 1); // Should be "wbarray=...".
		}	
		
		//System.out.println("foo: " + ba);
		
		byte[] foo = decodeByteArray(ba);
		
		if (foo.length != bs.length)
		{
			throw new SJIOException("[" + getTransportName() + "] Expected message size " + bs.length + "must currently match the received message size exactly: " + foo.length);
		}
	
		System.arraycopy(foo, 0, bs, 0, bs.length);
	}

	private String parsePostedMessage() throws SJIOException
	{
		try
		{
			String m;
			
			while (true)
			{
				m = br.readLine(); 
		
				if (m.startsWith(HTTP_CONTENT_LENGTH_HEADER)) // This is the final header. 
				{
					br.readLine(); // Empty line after final (Content-Length) header.
					
					int len = Integer.parseInt(m.substring((HTTP_CONTENT_LENGTH_HEADER + ": ").length()));
					
					char[] cs = new char[len];
					
					br.read(cs, 0, len); // Read the message (not terminated by a newline).
					
					return new String(cs);					
				}
			}
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	private void writeGet(String host, int port, String params, boolean close) throws SJIOException
	{
		try
		{
			bw.write("GET " + SJ_HTTP_SERVLET_PATH + "/" + SJ_HTTP_SERVLET_URL + "?" + params + " " + HTTP_VERSION + "\n");		
			bw.write(HTTP_HOST_HEADER + ": " + host + ":" + port + "\n");
			
			if (close)
			{
				bw.write(HTTP_CONNECTION_HEADER + ": " + HTTP_CONNECTION_CLOSE + "\n");
			}
			
			bw.write("\n");
			
			if (debug)
			{
				System.out.print("\nGET " + SJ_HTTP_SERVLET_PATH + "/" + SJ_HTTP_SERVLET_URL + "?" + params + " " + HTTP_VERSION + "\n");
				System.out.print(HTTP_HOST_HEADER + ": " + host + ":" + port + "\n");
				
				if (close)
				{
					System.out.print(HTTP_CONNECTION_HEADER + ": " + HTTP_CONNECTION_CLOSE + "\n");
				}
				
				System.out.print("\n");
			}
			
			bw.flush();
			
			if (usingSJServletProxy())
			{
				readHttpAck();
			}
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	private void writePost(String host, int port, String data) throws SJIOException
	{
		try
		{
			bw.write("POST " + SJ_HTTP_SERVLET_PATH + "/" + SJ_HTTP_SERVLET_URL + " " + HTTP_VERSION + "\n");			
			bw.write(HTTP_HOST_HEADER + ": " + host + ":" + port + "\n");
			bw.write(HTTP_USER_AGENT_HEADER + ": " + getTransportName() + "@" + getLocalHostName() + ":" + getLocalPort() + "\n");
			bw.write(HTTP_CONTENT_TYPE_HEADER + ": " + HTTP_FORM_URLENCODED_MEDIA_TYPE + "\n");
			bw.write(HTTP_CONTENT_LENGTH_HEADER + ": " + data.length() + "\n"); // FIXME: is this correct?
			bw.write("\n" + data);								
			bw.flush();
			
			if (debug)
			{
				System.out.print("\nPOST " + SJ_HTTP_SERVLET_PATH + "/" + SJ_HTTP_SERVLET_URL + " " + HTTP_VERSION + " \n");
				System.out.print(HTTP_HOST_HEADER + ": " + host + ":" + port + "\n");		
				System.out.print(HTTP_USER_AGENT_HEADER + ": " + getTransportName() + "@" + getLocalHostName() + ":" + getLocalPort() + "\n");			
				System.out.print(HTTP_CONTENT_TYPE_HEADER + ": " + HTTP_FORM_URLENCODED_MEDIA_TYPE + "\n");			
				System.out.print(HTTP_CONTENT_LENGTH_HEADER + ": " + data.length() + "\n");
				System.out.print("\n" + data + "\n");
			}
				
			if (usingSJServletProxy())
			{
				readHttpAck();
			}
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	private static final String HTTP_INTERNAL_SERVER_ERROR = "500";
	
	private void readHttpAck() throws SJIOException
	{
		try
		{
			String m;
			
			while (true)
			{
				m = br.readLine();
	
				if (debug)
				{
					System.out.println("Ack: " + m);
				}
	
				if (m.startsWith(HTTP_VERSION)) // FIXME: may need to allow for e.g. HTTP/1.0.
				{
					m = m.substring((HTTP_VERSION + " ").length());
					
					if (m.startsWith(HTTP_INTERNAL_SERVER_ERROR))
					{
						throw new SJIOException("[" + getTransportName() + "] Servlet connection terminally failed: " + m);
					}
				}
				else if (m.startsWith(HTTP_CONNECTION_HEADER)) // FIXME: re-establish connection? Don't rely on persistence.
				{
					m = m.substring((HTTP_CONNECTION_HEADER + " ").length());
					
					if  (m.startsWith(HTTP_CONNECTION_CLOSE))
					{
						throw new SJIOException("[" + getTransportName() + "] Servlet connection failed: " + m);
					}
				}				
				else if (m == null || m.length() == 0)
				{
					if (debug)
					{
						System.out.println();
					}
					
					return;
				}
			}
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	private String readResponse() throws SJIOException
	{
		String m = null;
		
		try
		{
			for (boolean done = false; !done; )
			{
				m = br.readLine();
	
				if (debug)
				{
					System.out.println("Response: " + m);
				}
					
				if (m == null)
				{
					throw new SJIOException("[" + getTransportName() + "] null value received from servlet proxy.");
				}
				else if (m.startsWith(FORWARDED_PREFIX)) // Messages forwarded by the servlet occupy a single line. OK to use newlines as delimiters because String messages have been serialized to bytes, and the bytes values are communicated as a String - so no newline character will appear within the message. 
				{
					String response = m.substring(FORWARDED_PREFIX.length(), m.length() - FORWARDED_SUFFIX.length());
					
					m = br.readLine();
					
					if (debug)
					{
						System.out.println("Response: " + m);
					}
					
					if (m.length() != 0) // This line had a single newline character (implicitly printed to the stream by the servlet after FORWARDED_SUFFIX).
					{
						throw new SJIOException("[" + getTransportName() + "] Expected newline, not: " + m);
					}
					
					if (debug)
					{
						System.out.println();
					}
					
					return response;
				}
			}
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
		
		return m;
	}	
	
	private String makeTargetParameters()
	{
		String data = "";
		
		data += SJ_HTTP_SERVLET_TARGET_HOST + "=" + getHostName();
		data += "&" + SJ_HTTP_SERVLET_TARGET_PORT + "=" + Integer.toString(getPort());
				
		return data;
	}	
	
	public void flush() throws SJIOException
	{	
		try
		{
			bw.flush();
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	public String getHostName()
	{	
		return hostName;
	}
	
	public int getPort()
	{	
		return port;
	}
	
	public String getLocalHostName()
	{	
		//return s.getLocalAddress().getHostName(); // FIXME: Java bug: gives 0.0.0.0. (Something to do with Windows IPv6).
		return localHostName;
	}
	
	public int getLocalPort()
	{	
		return s.getLocalPort();
	}	
	
    public boolean usingSJServletProxy()
	{
		return usingServletProxy;
	}
	
	public static String encodeByte(byte b)
	{
		return Byte.toString(b);
	}
	
	public static byte decodeByte(String m)
	{
		return Byte.parseByte(m);
	}
	
	public static String encodeByteArray(byte[] bs) // FIXME: optimise.
	{
		return Arrays.toString(bs).replace(" ", "");
	}
	
	public static byte[] decodeByteArray(String m) throws SJIOException // FIXME: optimise.
	{
		// May need to remove white space from m if the other side has flushed/closed? 
		try
		{		
			String[] ms = m.substring("[".length(), m.length() - "]".length()).split(","); 
			
			//System.out.println("bar: " + Arrays.toString(ms));
			
			byte[] bs = new byte[ms.length];
			
			for (int i = 0; i < bs.length; i++) 
			{
				bs[i] = Byte.parseByte(ms[i]);
			}		
			
			return bs;
		}
		catch (Exception x) // Hacky? Stops potential NumberFormatExceptions from breaking the close protocol (only seems to happen there).
		{
			throw new SJIOException(x);
		}
	}	
}
