package sessionj.runtime.net;

import sessionj.runtime.*;

import static sessionj.SJConstants.*;

public class SJIncompatibleSessionException extends SJException
{
	public static final long serialVersionUID = SJ_VERSION;

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
