//$ bin/sessionjc tests/src/simplechat/server/MessageLog.sj -d tests/classes/

package simplechat.server;

import java.util.ArrayList;

public class MessageLog // Effectively a synchronized ArrayList.
{	
	private ArrayList log = new ArrayList(); // Generics not yet done for this Polyglot version.

	public void addMessage(String msg)
	{
		synchronized (log)
		{
			try
			{
				log.add(msg);
			}
			finally
			{
				log.notifyAll();
			}
		}
	}	

	public String getMessage(int i) throws InterruptedException
	{
		synchronized (log)
		{	
			while (log.size() <= i) 
			{		
				log.wait();
			}

			return (String) log.get(i);
		}
	}
}
