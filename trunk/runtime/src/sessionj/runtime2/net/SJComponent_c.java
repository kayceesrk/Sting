package sessionj.runtime2.net;

abstract public class SJComponent_c implements SJComponent
{
	private SJComponentId cid; 
	
	protected SJComponent_c(SJComponentId cid)
	{
		this.cid = cid;
	}
	
	public final SJComponentId getComponentId()
	{
		return cid; 
	}
}
