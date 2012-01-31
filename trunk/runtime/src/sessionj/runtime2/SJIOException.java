package sessionj.runtime2;

import static sessionj.runtime2.SJRuntimeConstants.*;

public class SJIOException extends SJException
{
	public static final long serialVersionUID = SJ_RUNTIME_VERSION;

	public SJIOException()
	{
		super();
	}

	public SJIOException(String message)
	{
		super(message);
	}

	public SJIOException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SJIOException(Throwable cause)
	{
		super(cause);
	}
}
