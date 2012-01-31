package sessionj.runtime.session.contexts;

import sessionj.types.sesstypes.SJSessionType;

public class SJInwhileContext extends SJLoopContext
{
	public SJInwhileContext(SJSessionType active)
	{
		super(active);
	}

	/*public final boolean isInwhileContext()
	{
		return true;
	}*/
}
