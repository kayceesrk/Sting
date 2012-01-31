package sessionj.runtime;

//import static sessionj.SJConstants.*;

public class SJRuntimeException extends RuntimeException
{
	//public static final long serialVersionUID = SJ_VERSION; // Some problems with making a fresh build - mutual dependency between this class and some compiler classes? 
	public static final long serialVersionUID = 1; 

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
