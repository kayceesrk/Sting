//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/a/Peer.sj -d tests/classes/

package popl.bmarks.bmark2.a;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

class Peer extends Thread
{
	private boolean debug;	
	private Channel in;
	private Channel out;	
	private int len;
	
	private boolean init = false;
	private int size;
	
	public Peer(boolean debug, Channel in, Channel out, int len)
	{
		this.debug = debug;
		this.in = in;
		this.out = out;
		this.len = len;
	}	
	
	public Peer(boolean debug, Channel in, Channel out, int len, boolean init, int size)
	{
		this(debug, in, out, len);
		
		this.init = init;
		this.size = size;
	}
	
	public void run()
	{
		if (init)
		{
			out.write(new Message(size));
		}
		
		for (int i = 0; i < len; i++)
		{
			//for (int j = 0; j < 100; j++) { }
			
			Message msg = in.read();
			
			//msg.inc();
			msg = new Message(msg.getSize(), msg.getCount() + 1);
						
			out.write(msg);
						
			if (debug)
			{
				msg.println();
			}
		}
	}			
}
