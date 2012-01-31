package sessionj.runtime.transport.https;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJSessionParameters;
import sessionj.runtime.security.ssl.*;
import sessionj.runtime.transport.*;

/**
 * @author Andi Bejleri, Nuno Alves
 * 
 */
public class SJHTTPS extends AbstractSJAuthenticatingTransport
{
	public static final String TRANSPORT_NAME = "sessionj.runtime.transport.https.SJHTTPS";

	private static final int LOWER_PORT_LIMIT = 1024;
	private static final int PORT_RANGE = 10000;

	public SJConnectionAcceptor openAcceptor(int port, SJSessionParameters params) throws SJIOException
	{

		return new SJHTTPSAcceptor(port, this);
	}

	/*
	 * public SJConnection connect(SJServerIdentifier si) throws SJIOException{
	 * return connect(si.getHostName(), si.getPort()); }
	 */

	public SJConnection connect(String hostName, int port, String user,
			String pwd) throws SJIOException
	{
		try
		{
			// final SSLSocketFactory sslSocketFactory =
			// (SSLSocketFactory)SSLSocketFactory.getDefault();

			final MySSLSocketFactory sslSocketFactory = new MySSLSocketFactory(
					user, pwd);

			MySSLSocket conn = (MySSLSocket) sslSocketFactory.createSocket(
					hostName, port);

			if (conn == null)
				throw new SJIOException("[Client] Unable to connect to server");

			return new SJHTTPSConnection(conn, conn.getOutputStream(), conn.getInputStream(), this);
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}

	}

	public boolean portInUse(int port)
	{

		// SSLServerSocket sss = null;
		ServerSocket ss = null;

		try
		{
			/*
			 * final SSLServerSocketFactory sslSocketFactory =
			 * (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			 * 
			 * sss = (SSLServerSocket)sslSocketFactory.createServerSocket(port);
			 */

			ss = new ServerSocket(port);

		}
		catch (IOException ioe)
		{
			return true;
		}
		finally
		{
			// if (sss != null)
			if (ss != null)
			{
				try
				{
					// sss.close();
					ss.close();
				}
				catch (IOException ioe)
				{
				}
			}
		}

		return false;

	}

	public int getFreePort() throws SJIOException
	{

		int start = new Random().nextInt(PORT_RANGE);
		int seed = start + 1;

		for (int port = seed % PORT_RANGE; port != start; port = seed++
				% PORT_RANGE)
		{
			if (!portInUse(port + LOWER_PORT_LIMIT))
			{
				return port + LOWER_PORT_LIMIT;
			}
		}

		throw new SJIOException("[" + getTransportName()
				+ "] No free port available.");
	}

	public String getTransportName()
	{

		return TRANSPORT_NAME;
	}

	public String sessionHostToNegotiationHost(String hostName)
	{
		return hostName;
	}

	public int sessionPortToSetupPort(int port)
	{
		return port;
	}
}
