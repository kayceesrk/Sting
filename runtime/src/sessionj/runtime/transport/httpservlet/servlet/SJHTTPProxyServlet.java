//$ javac -cp compiler/classes/';'runtime/classes/';'lib/servlet-api.jar runtime/src/sessionj/runtime/transport/httpservlet/servlet/SJHTTPProxyServlet.java -d runtime/src/sessionj/runtime/transport/httpservlet/servlet/WEB-INF/classes/

package sessionj.runtime.transport.httpservlet.servlet;

import sessionj.SJConstants;
import sessionj.runtime.SJIOException;
import sessionj.runtime.SJRuntimeException;
import sessionj.runtime.net.SJRuntime;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.transport.SJConnection;
import sessionj.runtime.transport.SJTransportManager;
import sessionj.runtime.transport.SJTransportUtils;
import static sessionj.runtime.transport.httpservlet.SJHTTPServletConnection.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Raymond
 *
 * Need to allow for "Connection: close", i.e. Client needs to re-establish connection. Client should also use that header to clean up failed session connections here.
 *
 */
public class SJHTTPProxyServlet extends HttpServlet
{
	private static final long serialVersionUID = SJConstants.SJ_VERSION;

	public static final String FORWARDED_PREFIX = "<html><body>";
	public static final String FORWARDED_SUFFIX = "</body></html>"; // Don't include terminal newline here, because messes with lengths of readline results.
	
	public static final String CONNECTION_SUCCESS = "CONNECTION_SUCCESS";
	public static final String CONNECTION_FAIL = "CONNECTION_FAIL";
	public static final String IO_ERROR = "IO_ERROR";
	
	transient private static SJTransportManager sjtm = SJRuntime.getTransportManager();
	
	transient private static Map<String, SJConnection> negotiating = new HashMap<String, SJConnection>();
	transient private static Map<String, SJConnection> connections = new HashMap<String, SJConnection>(); // FIXME: need to clean up the connections for failed Client sessions. Ultimately, as for all connection-less (state-less) transports (e.g. UDP), need to use timeouts to determine whether the network or peer has crashed. 
	
	public void init(ServletConfig config) throws ServletException 
	{
		super.init(config);

        try {
            //sjtm.loadNegotiationTransports("m");
            //sjtm.loadSessionTransports("m");
        	sjtm.loadNegotiationTransports(SJTransportUtils.parseTransportFlags("m"));
          sjtm.loadNegotiationTransports(SJTransportUtils.parseTransportFlags("m"));
        } catch (SJIOException e) {
            throw new ServletException(e);
        }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter pw = new PrintWriter(response.getOutputStream()); // Also supposed to set the Content-Length header to permit persistent connections (although this is done automatically for small enough responses).		
		
		String key = null;
		
		try
		{
			key = parseKey(request);			
			
			String connectionHeader = request.getHeader(HTTP_CONNECTION_HEADER); 
			
			if (connectionHeader != null)
			{
				if (connectionHeader.startsWith(HTTP_CONNECTION_CLOSE))
				{
					throw new SJIOException("[SJHTTPProxyServlet] Connection closed by Client."); // To be caught below - connection will be closed - and then re-thrown as IOException.
				}
			}
			else
			{
				if (request.getParameter(SJ_HTTP_SERVLET_FIN) != null) // Shutdown.
				{
					doFin(key, pw);
				}			
				else if (request.getParameter(SJ_HTTP_SERVLET_READ_BYTE) != null) // Read polling.
				{
					doWriteByte(key, pw); // We are writing to the Client.
				}
				else 
				{
					String len = request.getParameter(SJ_HTTP_SERVLET_READ_BYTE_ARRAY);
					
					if (len != null) // Read polling.			
					{
						doWriteByteArray(key, pw, Integer.parseInt(len));
					}
					else // Initialisation (currently, no explicit signal header).
					{	
						String targetHostName = request.getParameter(SJ_HTTP_SERVLET_TARGET_HOST);
						int targetPort = Integer.parseInt(request.getParameter(SJ_HTTP_SERVLET_TARGET_PORT));	
						
						doInit(key, pw, targetHostName, targetPort);	
					}
				}
			}
		}
		catch (SJIOException ioe)
		{
			pw.println(FORWARDED_PREFIX + IO_ERROR + FORWARDED_SUFFIX);			

			if (key != null)
			{
				SJConnection conn = getNegotiating(key);
				
				if (conn != null)
				{
					sjtm.closeConnection(conn);
				}
				else
				{
					closeConnection(key);
				}
			}
			
			throw new IOException(ioe);			
		}
		finally
		{
			pw.println();
			//pw.flush(); // Interferes with our protocol?
			pw.close(); // Doesn't seem to breaks persistence (in fact seems to be needed to make the response send properly).
		}
	}
	
	private String parseKey(HttpServletRequest request) throws SJIOException
	{	
		String key;
		
		String th = request.getParameter(SJ_HTTP_SERVLET_TARGET_HOST);
		String tp = request.getParameter(SJ_HTTP_SERVLET_TARGET_PORT);
		/*String[] lh = (String[]) params.get(SJ_HTTP_SERVLET_LOCAL_HOST); 
		String[] lp = (String[]) params.get(SJ_HTTP_SERVLET_LOCAL_PORT);*/
		
		if (th == null || tp == null/* || lh == null || lp == null*/)
		{
			throw new SJIOException("[SJHTTPProxyServlet] Invalid parameters: " + th + ", " + tp);
		}
		
		String targetHostName = th;
		int targetPort = Integer.parseInt(tp);

		/*String clientHostName = lh[0]; // Redundant.
		int clientPort = Integer.parseInt(lp[0]);*/ // FIXME: Java bug? ServletRequest.getRemotePort doesn't match this value (seems to be one greater than this value).
		
		String clientHostName = request.getRemoteAddr(); // FIXME: host names are more fragile than IP addresses (e.g. HZHL2 on IC-DoC).
		int clientPort = request.getRemotePort();
		
		key = targetHostName + ":" + Integer.toString(targetPort) + ";" + clientHostName + ":" + Integer.toString(clientPort);
		
		return key;
	}
	
	private void doInit(String key, PrintWriter pw, String targetHostName, int targetPort) throws SJIOException
	{
		try
		{
			SJConnection conn = sjtm.openConnection(targetHostName, targetPort, SJSessionParameters.DEFAULT_PARAMETERS); // FIXME: whose transport preferences should we use? // FIXME: maybe should use Client's preferences - currently, the Server must also be using the SJManualSerializer wire protocol as for the SJHTTPServletConnection. Currently using the defaults (SJManualTCP) configured on static initialisation. 
	
			addNegotiating(key, conn); // FIXME: Factor out the following - we now need to manually run the equivalent of the server-side transport manager open connection protocol.
			
			pw.println(FORWARDED_PREFIX + CONNECTION_SUCCESS + FORWARDED_SUFFIX);
			
			// Server side will now be expecting initiation protocol, etc. This should be coming next from the Client side.
		}
		catch (SJIOException ioe)
		{
			pw.println(FORWARDED_PREFIX + CONNECTION_FAIL + FORWARDED_SUFFIX);
		}
	}
	
	private void doNegotiationWrite(String key, PrintWriter pw) // Client will do the preliminary negotiation read after the write. // We are writing a byte.
	{
		pw.println(FORWARDED_PREFIX + SJTransportManager.SJ_SERVER_TRANSPORT_FORCE + FORWARDED_SUFFIX);
		
		promoteNegotiating(key);  
	}
	
	private void doWriteByte(String key, PrintWriter pw) throws SJIOException 
	{
		SJConnection conn = getConnection(key);
		
		if (conn == null/* || conn.isClosed()*/) // FIXME: desperately need an isClosed.
		{
			if (getNegotiating(key) != null)
			{
				doNegotiationWrite(key, pw);
			}
			else
			{
				pw.println(FORWARDED_PREFIX + IO_ERROR + FORWARDED_SUFFIX);
			}
		}
		else
		{		
			byte b;
			
			synchronized (conn)
			{
				b = conn.readByte();
			}
					
			pw.println(FORWARDED_PREFIX + Byte.toString(b) + FORWARDED_SUFFIX); // The suffix has an implicit newline at the end.
		}
		
		//return b;
	}
	
	// Called write because we're writing to the Client. But it is in repsonse to a GET read request from the Client.
	private void doWriteByteArray(String key, PrintWriter pw, int len) throws SJIOException
	{
		SJConnection conn = getConnection(key);
		
		if (conn == null/* || conn.isClosed()*/) // FIXME: desperately need an isClosed.
		{
			pw.println(FORWARDED_PREFIX + IO_ERROR + FORWARDED_SUFFIX);
		}					
		else
		{
			byte[] bs = new byte[len]; // Could factor out encoding of integer.
			
			synchronized (conn)
			{												
				conn.readBytes(bs);	// readFully, as per ATI specification				
			}
	
			pw.println(FORWARDED_PREFIX + Arrays.toString(bs).replace(" ", "") + FORWARDED_SUFFIX);
		}
		
		//return bs;
	}
	
	private void doFin(String key, PrintWriter pw) throws SJIOException 
	{
		closeConnection(key);
	}	
	
	private void doNegotiationRead(String key, byte b) // Client will do the preliminary negotiation write first. 
	{
		if (b == SJTransportManager.SJ_CLIENT_TRANSPORT_NEGOTIATION_NOT_NEEDED || b == SJTransportManager.SJ_CLIENT_TRANSPORT_NEGOTIATION_START)
		{
			//promoteNegotiating(key); // Promote when the Client is waiting for our SJ_SERVER_TRANSPORT_SUPPORTED (this relies on the Client performing the negotiation read after the write). Any potential concurrency problems, i.e. the negotiation doGet being processes before the doPost? This may not be a problem actually. 
		}
		else //if (b == SJTransportManager.SJ_CLIENT_TRANSPORT_NEGOTIATION_START)
		{
			throw new SJRuntimeException("[SJHTTPProxyServlet] Negotiation past SJHTTPProxyServlet setup not yet supported.");
		}
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String key = null;
		
		try
		{			
			key = parseKey(request);			
			
			SJConnection conn = getConnection(key);
						
			String rbyte = request.getParameter(SJ_HTTP_SERVLET_WRITE_BYTE);
			String rbarray = request.getParameter(SJ_HTTP_SERVLET_WRITE_BYTE_ARRAY);
			
			if (rbyte != null) // doReadByte. // We are reading from the Client.
			{
				byte b = decodeByte(rbyte);
				
				if (conn == null)
				{
					/*if (getNegotiating(key) == null)
					{
						// Errors are detected by the Client on read, i.e. handled in doGet.
					}*/
					
					doNegotiationRead(key, b);
				}
				else
				{				
					conn.writeByte(b);
				}
			}
			else //if (rbarray != null) // doReadByteArray.
			{
				/*if (conn == null)
				{
					// Errors are detected by the Client on read, i.e. handled in doGet.
				}*/
				
				byte[] bs = decodeByteArray(rbarray);
				
				conn.writeBytes(bs);
			} 
		}
		catch (SJIOException ioe)
		{
			if (key != null)
			{
				closeConnection(key);
			}
			
			throw new IOException(ioe);
		}		
		finally
		{

		}
	}
	
	private void addNegotiating(String key, SJConnection conn)
	{
		synchronized (negotiating)
		{
			negotiating.put(key, conn);
		}
	}
	
	private SJConnection getNegotiating(String key)
	{
		synchronized (negotiating)
		{
			return negotiating.get(key);
		}
	}	
	
	/*private void closeNegotiating(String key) 
	{
		synchronized (negotiating)
		{
			if (negotiating.containsKey(key))
			{
				sjtm.closeConnection(negotiating.remove(key)); // FIXME: correct?
			}
		}
	}*/
	
	private void promoteNegotiating(String key) 
	{
		synchronized (negotiating)
		{
			addConnection(key, negotiating.remove(key)); // Synchronised on connections.
		}
	}
	
	private void addConnection(String key, SJConnection conn)
	{
		synchronized (connections)
		{
			connections.put(key, conn);
		}
	}
	
	private SJConnection getConnection(String key)
	{
		synchronized (connections)
		{
			return connections.get(key);
		}
	}	
	
	private void closeConnection(String key) 
	{
		synchronized (connections)
		{
			if (connections.containsKey(key))
			{
				sjtm.closeConnection(connections.remove(key)); // FIXME: correct?
			}
		}
	}
}
