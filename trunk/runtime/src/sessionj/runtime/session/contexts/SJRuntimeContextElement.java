package sessionj.runtime.session.contexts;

import sessionj.types.sesstypes.SJSessionType;

public abstract class SJRuntimeContextElement
{
	private static final int CONTEXT_CLASS_PREFIX = "class sessionj.runtime.session.contexts.".length(); // Factor out constants.
	private static final int CONTEXT_CLASS_SUFFIX = "Context".length();
	
	private SJSessionType active;
	//private SJSessionType current; // Currently performing.
	private SJSessionType implemented; // Performed.

	//private SJSessionType original;
	
	public SJRuntimeContextElement(SJSessionType active)
	{		
		this.active = (active == null) ? null : active.treeClone();
		
		//this.original = (active == null) ? null : active.treeClone();
	}

	public SJSessionType activeType()
	{
		return active;
	}

	public final SJRuntimeContextElement activeType(SJSessionType active)
	{
		this.active = active;

		return this;
	}

	/*public SJSessionType currentType()
	{
		return current;
	}

	public SJSocketContext currentType(SJSessionType current)
	{
		this.current = current;

		return this;
	}*/

	public final SJSessionType implementedType()
	{
		return implemented;
	}

	public final SJRuntimeContextElement implementedType(SJSessionType implemented)
	{
		this.implemented = implemented;

		return this;
	}

	public String toString()
	{
		String m = this.getClass().toString();

		return m.substring(CONTEXT_CLASS_PREFIX, m.length() - CONTEXT_CLASS_SUFFIX);
	}

	/*public SJSessionType originalType()
	{
		return original;
	}*/
	
	/*public boolean isTopLevelContext()
	{
		return false;
	}

	public boolean isBranchContext()
	{
		return false;
	}

	public boolean isOutbranchContext()
	{
		return false;
	}

	public boolean isInbranchContext()
	{
		return false;
	}

	public boolean isIterationContext()
	{
		return false;
	}

	public boolean isOutwhileContext()
	{
		return false;
	}

	public boolean isInwhileContext()
	{
		return false;
	}

	public boolean isRecursionContext()
	{
		return false;
	}*/
}
