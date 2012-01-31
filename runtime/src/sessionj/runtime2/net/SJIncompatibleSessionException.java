package sessionj.runtime2.net;

import sessionj.runtime2.*;

import static sessionj.runtime2.SJRuntimeConstants.*;

public class SJIncompatibleSessionException extends SJIOException
{
	public static final long serialVersionUID = SJ_RUNTIME_VERSION;

	public SJIncompatibleSessionException()
	{
		super();
	}

	public SJIncompatibleSessionException(String message)
	{
		super(message);
	}

	public SJIncompatibleSessionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SJIncompatibleSessionException(Throwable cause)
	{
		super(cause);
	}
}
