/**
 * 
 */
package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

/**
 * @author Raymond
 *
 */
public class SJSessionAcceptorThread extends SJAcceptorThread {

    private boolean isAccepting = false;
	
	public SJSessionAcceptorThread(SJAcceptorThreadGroup atg, SJConnectionAcceptor ca)
	{
		super(atg, "SessionAcceptorThread: " + ca.getTransportName() + ":" + atg.getPort(), ca);

    }
	
	public void run()
	{
        isAccepting = true;
		
		try
		{
			while (run)
			{
				try
				{
					atg.queueConnection(ca.accept()); // just a simple version of SJSetupThread. can probably combine into one class.
				}
				//catch (InterruptedException ie) { } // Interrupt doesn't apply...
				catch (SJIOException ioe) { } // ...instead close the connection acceptor and catch the exception?			
			}
		}
		finally
		{
            isAccepting = false;
			
			if (!ca.isClosed())
			{
				ca.close(); 
			}
		}	
	}
	
	public int getPort()
	{
		return atg.getPort();
	}
	
	public boolean isAccepting()
	{
		return isAccepting;
	}
	
	public String toString()
	{
		return "SJAcceptor " + super.toString();
	}

}
