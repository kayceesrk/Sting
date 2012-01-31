//$ javac -cp tests/classes/ tests/src/qe/session/Session.java -d tests/classes/

package qe.session;

import qe.channel.*;

abstract public class Session extends Thread
{
	protected boolean debug;

	protected boolean isRequestor;

	protected Channel in;
	protected Channel out;

	protected int size;
	protected int len;

	//public Session(boolean isRequestor, Channel in, Channel out)
	public Session(boolean debug, Channel in, Channel out)
	{
		this.debug = debug;

		this.in = in;
		this.out = out;
	}

	public final boolean isDebug()
	{
		return debug;
	}

	public final boolean isRequestor()
	{
		return isRequestor;
	}

	public final int getMessageSize()
	{
		return size;
	}

	public final int getSessionLength()
	{
		return len;
	}

	public void startAcceptor(int size, int len)
	{
		this.size = size;
		this.len = len;
		this.isRequestor = false;

		start();
	}

	public void startRequestor(int size, int len)
	{
		this.size = size;
		this.len = len;
		this.isRequestor = true;

		start();
	}
}
