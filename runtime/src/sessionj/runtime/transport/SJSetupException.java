package sessionj.runtime.transport;

import sessionj.runtime.SJIOException;

import static sessionj.SJConstants.SJ_VERSION;

public class SJSetupException extends SJIOException
{
	public static final long serialVersionUID = SJ_VERSION;

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
