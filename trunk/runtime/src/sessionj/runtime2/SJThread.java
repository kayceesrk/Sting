package sessionj.runtime2;

abstract public class SJThread
{
	public SJThread spawn()
	{
		throw new SJRuntimeException("[SJThread] Shouldn't get in here: bad compiler translation.");
	}
}
