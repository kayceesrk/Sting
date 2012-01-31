package sessionj.runtime.session.contexts;

import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJLabel;

public class SJOutbranchContext extends SJBranchContext
{
	public SJOutbranchContext(SJLabel lab, SJSessionType active)
	{
		super(lab, active);
	}

	/*public final boolean isOutbranchContext()
	{
		return true;
	}*/
}
