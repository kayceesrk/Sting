package sessionj.runtime.net;

public class SJSelectorCloser
{
	private SJSelectorImpl s; // FIXME: would be better for SJSelector.   
	
	protected SJSelectorCloser(SJSelectorImpl s)
	{
		this.s = s;
	}
	
	public void close()
	{
		try
		{
			this.s.close();
		}
		catch (Exception x) // FIXME.
		{
			x.printStackTrace();
		}
	}
}
