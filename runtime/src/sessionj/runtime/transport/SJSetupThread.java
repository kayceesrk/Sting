/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;
import sessionj.runtime.net.SJRuntime;

/**
 * @author Raymond
 *
 */
public class SJSetupThread extends SJAcceptorThread
{	
	public SJSetupThread(SJAcceptorThreadGroup atg, SJConnectionAcceptor ca)
	{
		super(atg, "NegotiationAcceptorThread: " + ca.getTransportName() + ":" + atg.getPort(), ca);
	}
	
	public void run()
	{		
		try
		{
			while (run)
			{
				SJConnection conn = null;
				
				boolean reuse = false;
				
				try
				{
					reuse = false;
					
					conn = ca.accept();
					
					reuse = SJRuntime.getTransportManager().serverNegotiation(atg.getParameters(), atg, conn); // Can also get the transport manager from atg.
					
					if (reuse)
					{
						atg.queueConnection(conn);
					}					
				}			
				catch (SJIOException ioe)
				{
					
				}
				finally
				{
					if (!reuse)
					{
						if (conn != null)
						{
							try
							{
								conn.flush();
							}
							catch (SJIOException ioe)
							{
								
							}
							finally
							{
								conn.disconnect();
							}
						}						
					}
				}
			}
		}
		finally
		{
			if (!ca.isClosed())
			{
				ca.close(); 
			}
		}			
	}

    public String toString()
	{
		return "SJSetup" + super.toString();
	}	
}
