package sessionj.runtime;

public abstract class SJException extends Exception
{
	protected SJException()
	{
    }

	protected SJException(String message)
	{
		super(message);
	}

	protected SJException(String message, Throwable cause)
	{
		super(message, cause);
	}

	protected SJException(Throwable cause)
	{
		super(cause);
	}
}
