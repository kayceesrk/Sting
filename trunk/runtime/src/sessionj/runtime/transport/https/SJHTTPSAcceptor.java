package sessionj.runtime.transport.https;

import java.io.IOException;

import sessionj.runtime.SJIOException;
import sessionj.runtime.security.ssl.*;
import sessionj.runtime.transport.*;

/**
 * @author Andi Bejleri, Nuno Alves
 * 
 */
public class SJHTTPSAcceptor extends AbstractWithTransport implements SJConnectionAcceptor
{
	private MySSLServerSocket sss;

	public SJHTTPSAcceptor(int port, SJTransport transport) throws SJIOException
	{
		super(transport);
		
		try
		{
			// final SSLServerSocketFactory sslSocketFactory =
			// (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
			final MySSLServerSocketFactory sslSocketFactory = new MySSLServerSocketFactory();

			sss = (MySSLServerSocket) sslSocketFactory.createServerSocket(port); // Didn't
																																						// bother
																																						// to
																																						// explicitly
																																						// check
																																						// portInUse.
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	public SJConnection accept() throws SJIOException
	{
		try
		{
			if (sss == null)
			{
				throw new SJIOException("[" + getTransportName()
						+ "] Connection acceptor not open.");
			}

			MySSLSocket s = (MySSLSocket) sss.accept();

			return new SJHTTPSConnection(s, s.getOutputStream(), s.getInputStream(), getTransport());
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

	public void close()
	{
		try
		{
			if (sss != null)
			{
				sss.close();
			}
		}
		catch (IOException ioe)
		{
			
		}
	}

	public boolean interruptToClose()
	{
		return false;
	}

	public boolean isClosed()
	{
		return sss.isClosed();
	}
}
