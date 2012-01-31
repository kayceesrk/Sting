//$ javac -cp tests/classes/ tests/src/qe/session/QESession.java -d tests/classes/

package qe.session;

import qe.channel.*;

abstract public class QESession extends Thread
{
	protected boolean debug;

	protected boolean isRequestor;

	protected QECell[] ins;
	protected QECell[] outs;

	protected int size;
	protected int len;

	//public QESession(boolean isRequestor, Channel in, Channel out)
	public QESession(boolean debug, QECell[] ins, QECell[] outs)
	{
		this.debug = debug;

		this.ins = ins;
		this.outs = outs;
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
