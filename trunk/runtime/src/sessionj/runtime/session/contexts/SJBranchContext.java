package sessionj.runtime.session.contexts;

import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJLabel;

public abstract class SJBranchContext extends SJRuntimeContextElement
{
	private SJLabel lab; // This is a session-branch context.

	public SJBranchContext(SJLabel lab, SJSessionType active)
	{
		super(active);

		this.lab = lab;
	}

	public SJLabel label()
	{
		return lab;
	}

	/*public final boolean isBranchContext()
	{
		return true;
	}*/
}
