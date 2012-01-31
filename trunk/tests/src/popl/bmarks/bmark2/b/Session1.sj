//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/b/Session.sj -d tests/classes/

package popl.bmarks.bmark2.b;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

// Bidirectional exchanges between Requestor and Acceptor. 
class Session1 extends Session
{
	final boolean debug;
	
	int size;
	
	public Session1(boolean debug, boolean requestor, Channel in, Channel out)
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
				
				msg = in.read();
			}
		}
		else
		{
			Message msg;
			
			for (int i = 0; i < len; i++)
			{
				msg = in.read();
				
				msg = new Message(msg.getSize(), msg.getCount() + 1);
				
				out.write(msg);
				
				if (debug)
				{
					msg.println();
				}				
			}
		}				
	}
}
	