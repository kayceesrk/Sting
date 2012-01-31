/**
 * 
 */
package sessionj.runtime2.transport;

import sessionj.runtime2.*;

/**
 * @author Raymond
 *
 */
public class SJAcceptorThread extends Thread
{
	private SJAcceptorThreadGroup atg; 
	private SJConnectionAcceptor ca;
	
	private boolean run = false;
	
	public SJAcceptorThread(SJAcceptorThreadGroup atg, SJConnectionAcceptor ca)
	{
		super(atg, ca.getTransportId() + ":" + atg.getSessionPort());
		
		this.atg = atg;
		this.ca = ca;				
	}
	
	public void run()
	{
		this.run = true;
		
		try
		{
			while (run)
			{
				try
				{
					atg.appendToConnectionQueue(ca.accept());
				}
				//catch (InterruptedException ie) { } // Interrupt doesn't apply...
				catch (SJIOException ioe) { } // ...instead close the connection acceptor and catch the exception?			
			}
		}
		finally
		{
			this.run = false;
			
			if (!ca.isClosed())
			{
				ca.close(); 
			}
		}	
	}
	
	public void close()
	{
		run = false;
		
		ca.close();
		
		if (ca.interruptToClose())
		{
			this.interrupt();
		}
	}
	
	public int getSessionPort()
	{
		return atg.getSessionPort();
	}
	
	public int getPort()
	{
		return ca.getPort();
	}
	
	public boolean isRunning()
	{
		return run;
	}
	
	public String toString()
	{
		return "SJAcceptor:" + super.toString();
	}
}
