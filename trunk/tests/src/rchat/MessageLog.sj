package rchat;

import java.util.ArrayList;

class MessageLog // A synchronized ArrayList.
{	
	private ArrayList msglog = new ArrayList(); // Generics not yet done for this Polyglot version.

	public void putmsg(String msg)
	{
		synchronized (msglog)
		{
			try
			{
				msglog.add(msg);
			}
			finally
			{
				msglog.notifyAll();
			}
		}
	}	

	public String getmsg(int i) throws InterruptedException
	{
		synchronized (msglog)
		{	
			while (msglog.size() <= i) // Got this wrong initially - subsequent exception swallowing bad.
			{		
				msglog.wait();
			}

			return (String) msglog.get(i);
		}
	}
}
