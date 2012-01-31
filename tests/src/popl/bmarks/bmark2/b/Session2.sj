//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/b/Session2.sj -d tests/classes/

package popl.bmarks.bmark2.b;

import java.util.*;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

// Unidirectional communication from Requestor to Acceptor.
public class Session2 extends Session
{
	final boolean debug;
	
	int size;
	
	public Session2(boolean debug, boolean requestor, Channel in, Channel out)
	{
		super(requestor, in, out);
		
		this.debug = debug;
	}
	
	public void start(int len, int size)
	{
		super.start(len);
		
		this.size = size;
	}	
	
	public void run()
	{
		if (requestor)
		{
			Message msg;
			
			for (int i = 0; i < len; i++)
			{
				msg = new Message(size, i);
				
				out.write(msg);

				if (debug)
				{
					msg.println();
				}
			}
		}
		else
		{
			Message msg;
			
			for (int i = 0; i < len; i++)
			{
				msg = in.read();
								
				if (debug)
				{
					msg.println();
				}					
			}
		}				
	}
}	
