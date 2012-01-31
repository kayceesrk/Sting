package sessionj.runtime.net;

import sessionj.runtime.*;

import static sessionj.SJConstants.*;

public class SJTimeoutException extends SJIOException
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJTimeoutException()
	{
		super();
	}

	public SJTimeoutException(String message)
	{
		super(message);
	}

	public SJTimeoutException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SJTimeoutException(Throwable cause)
	{
		super(cause);
	}
}
