package sessionj.runtime2;

import static sessionj.runtime2.SJRuntimeConstants.*;

public class SJRuntimeException extends RuntimeException
{
	public static final long serialVersionUID = SJ_RUNTIME_VERSION;

	public SJRuntimeException()
	{
		super();
	}

	public SJRuntimeException(String message)
	{
		super(message);
	}

	public SJRuntimeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SJRuntimeException(Throwable cause)
	{
		super(cause);
	}
}
