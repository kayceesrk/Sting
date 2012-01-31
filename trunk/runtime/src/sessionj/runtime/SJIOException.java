package sessionj.runtime;

import static sessionj.SJConstants.*;

public class SJIOException extends SJException
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJIOException()
	{
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
