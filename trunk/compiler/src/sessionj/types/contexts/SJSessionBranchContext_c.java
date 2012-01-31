/**
 * 
 */
package sessionj.types.contexts;

import java.util.List;

import sessionj.ast.sessops.compoundops.*;

/**
 * @author Raymond
 *
 */
public class SJSessionBranchContext_c extends SJBranchContext_c implements SJSessionBranchContext
{
	private SJBranchOperation bo; // Basically duplicated from (deprecated) SJSessionContext_c because single inheritance restriction.
	private List<String> targets;
	
	/**
	 * 
	 */
	public SJSessionBranchContext_c(SJContextElement ce, SJBranchOperation bo, List<String> targets)
	{
		super(ce);
		
		this.bo = bo;
		this.targets = targets;
	}

	public SJCompoundOperation node()
	{
		return bo;
	}
	
	/* (non-Javadoc)
	 * @see sessionj.types.contexts.SJSessionContext#targets()
	 */
	public List<String> targets()
	{
		return targets;
	}
	
	public boolean hasSessionImplementations()
	{
		return true;
	}
}
