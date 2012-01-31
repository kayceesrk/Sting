//$ bin/sessionjc -cp tests/classes/ tests/src/popl/bmarks/bmark2/b/Session.sj -d tests/classes/

package popl.bmarks.bmark2.b;

import popl.bmarks.*;
import popl.bmarks.bmark2.*;

abstract class Session extends Thread
{	
	protected boolean requestor;
	protected Channel in;
	protected Channel out;	
	
	protected int len;
	
	public Session(boolean requestor, Channel in, Channel out)
	{
		this.requestor = requestor;
		this.in = in;
		this.out = out;
	}
	
	public void start(int len)
	{
		this.len = len;
		
		this.start();
	}
	
	abstract public void start(int len, int size);
}
