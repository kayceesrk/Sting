package sessionj.runtime;

abstract public class SJThread 
{
	public SJThread spawn()
	{
		throw new SJRuntimeException("[SJThread] Shouldn't get in here.");
	}
}
