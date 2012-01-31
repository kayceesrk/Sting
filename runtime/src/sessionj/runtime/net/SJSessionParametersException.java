package sessionj.runtime.net;

import sessionj.runtime.SJException;

import static sessionj.SJConstants.*;

public class SJSessionParametersException extends SJException
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJSessionParametersException()
	{
   
	}

	public SJSessionParametersException(String message)
	{
		super(message);
	}

	public SJSessionParametersException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SJSessionParametersException(Throwable cause)
	{
		super(cause);
	}
}
