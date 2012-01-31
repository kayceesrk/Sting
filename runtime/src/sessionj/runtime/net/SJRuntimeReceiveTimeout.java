package sessionj.runtime.net;

import sessionj.runtime.SJRuntimeException;

class SJRuntimeReceiveTimeout extends Thread
// This is a bit of a hack - maybe better to have timeout operations directly supported by each transport,
// by adding them to the ATI. This scheme currently relies on the implicit socket close to terminate this thread (if it's still blocking) after a timeout.
{
	public static final int OBJECT = 11;
	public static final int INT = 12;
	public static final int BOOLEAN = 13;
	public static final int DOUBLE = 14;

	public static final int CHANNEL = 21;
	public static final int SESSION = 22;

	private Thread t;
	private SJSocket s;
	private final Object[] res;
	private int op;
	private Object[] args;

	public SJRuntimeReceiveTimeout(Thread t, SJSocket s, Object[] res, int op, Object[] args)
	{
		this.t = t;
		this.s = s;
		this.res = res;
		this.op = op;
		this.args = args;
	}

	public void run()
	{
		Object o;

		try
		{
			switch (op)
			{
				case OBJECT:
				{
					o = s.receive(); break;
				}
				case INT:
				{
					o = s.receiveInt(); break;
				}
				case BOOLEAN:
				{
					o = s.receiveBoolean(); break;
				}
				case DOUBLE:
				{
					o = s.receiveDouble(); break;
				}
				case CHANNEL:
				{
					o = s.receiveChannel((String) args[0]); break;
				}
				case SESSION:
				{
					o = s.receiveSession((String) args[0], (SJSessionParameters) args[1]); break;
				}
				default:
				{
					o = new SJRuntimeException("[SJRuntimeReceiveTimeout] Shouldn't get in here: " + op);
				}
			}
		}
		catch (Exception x)
		{
			o = x;
		}

		synchronized (res)
		{
			res[0] = o;
		}

		t.interrupt();
	}
}
