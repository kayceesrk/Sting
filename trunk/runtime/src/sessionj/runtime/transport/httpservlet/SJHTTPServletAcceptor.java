package sessionj.runtime.transport.httpservlet;

import java.io.*;
import java.net.*;
import java.nio.channels.SelectableChannel;

import sessionj.runtime.*;
import sessionj.runtime.transport.*;

public class SJHTTPServletAcceptor extends AbstractWithTransport implements SJConnectionAcceptor
{	
	private ServerSocket ss;

    public SJHTTPServletAcceptor(int port, SJTransport transport) throws SJIOException
	{
		super(transport);
        try
		{
			ss = new ServerSocket(port); // Didn't bother to explicitly check portInUse.
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}
	
	public SJConnection accept() throws SJIOException{
		
		try
		{
			if (ss == null)
			{
				throw new SJIOException("[" + getTransportName() + "] Connection acceptor not open.");
			}
			
			Socket s = ss.accept(); 
			
			return new SJHTTPServletConnection(s, getTransport());
		}
		catch (IOException ioe)
		{
			throw new SJIOException(ioe);
		}
	}

    public SelectableChannel acceptSelectableChannel() {
        throw new UnsupportedOperationException("TODO");
    }

    public void close()
	{	
		try 
		{ 
			if (ss != null)
			{
				ss.close(); 
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
		return ss.isClosed();
	}
}
