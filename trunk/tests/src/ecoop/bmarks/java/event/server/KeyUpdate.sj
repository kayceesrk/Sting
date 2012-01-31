package ecoop.bmarks.java.event.server;

import java.nio.*;
import java.nio.channels.*;

class KeyUpdate 
{
	public static final int REGISTER = 1; // Not currently used.
	public static final int CHANGE_OPS = 2;

	private SocketChannel sc;
	private int action;
	private int ops;

	public KeyUpdate(SocketChannel sc, int action, int ops)
	{
		this.sc = sc;
		this.action = action;
		this.ops = ops;
	}

	public SocketChannel getSocketChannel()
	{
		return sc;
	}

	public int getAction()
	{
		return action;
	}

	public int getInterestOps()
	{
		return ops;
	}
}
