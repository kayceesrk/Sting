package sessionj.runtime.session.contexts;

import sessionj.types.sesstypes.SJSessionType;

public class SJTopLevelContext extends SJRuntimeContextElement
{
	public SJTopLevelContext (SJSessionType active)
	{
		super(active);
	}

	/*public final boolean isTopLevelContext()
	{
		return true;
	}*/
}
