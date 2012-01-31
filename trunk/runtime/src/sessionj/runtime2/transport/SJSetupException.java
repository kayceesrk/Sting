package sessionj.runtime2.transport;

import sessionj.runtime2.*;

import static sessionj.runtime2.SJRuntimeConstants.SJ_RUNTIME_VERSION;

public class SJSetupException extends SJIOException
{
	public static final long serialVersionUID = SJ_RUNTIME_VERSION;

	public SJSetupException()
	{
		super();
	}

	public SJSetupException(String message)
	{
		super(message);
	}

	public SJSetupException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SJSetupException(Throwable cause)
	{
		super(cause);
	}	
}
