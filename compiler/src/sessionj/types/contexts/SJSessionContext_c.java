/**
 * 
 */
package sessionj.types.contexts;

import java.util.List;

import sessionj.ast.sessops.compoundops.SJCompoundOperation;

/**
 * @author Raymond
 *
 * @deprecated
 *
 */
abstract public class SJSessionContext_c extends SJContextElement_c implements
    SJSessionContext
{
	private SJCompoundOperation co;	
	private List<String> targets;
	
	/**
	 * 
	 */
	public SJSessionContext_c(SJCompoundOperation co, List<String> targets)
	{
		super();
		
		this.co = co;
		this.targets = targets; // Could get the targets from the operation node.
	}

	/**
	 * @param ce
	 */
	public SJSessionContext_c(SJContextElement ce)
	{
		super(ce);
	}

	public SJCompoundOperation node()
	{
		return co;
	}
	
	/* (non-Javadoc)
	 * @see sessionj.types.contexts.SJSessionContext#targets()
	 */
	public List<String> targets()
	{
		return targets;
	}
}
