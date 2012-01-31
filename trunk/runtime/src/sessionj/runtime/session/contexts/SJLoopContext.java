package sessionj.runtime.session.contexts;

import sessionj.types.sesstypes.SJSessionType;

public abstract class SJLoopContext extends SJRuntimeContextElement // Also for tail recursion.
{
	//private SJSessionType original; // Could also store information like how many times iterated.

	public SJLoopContext(SJSessionType active)
	{
		super(active);

		//this.original = (active == null) ? null : active.treeClone();
	}

	/*public SJSessionType originalType()
	{
		return original;
	}*/

	/*public final boolean isIterationContext()
	{
		return true;
	}*/
}
