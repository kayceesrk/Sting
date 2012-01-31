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
public class SJSessionLoopContext_c extends SJLoopContext_c implements
    SJSessionLoopContext
{
	private SJLoopOperation lo; // Basically duplicated from (deprecated) SJSessionContext_c because single inheritance restriction.
	private List<String> targets;
	
	/**
	 * 
	 */
	public SJSessionLoopContext_c(SJContextElement ce, SJLoopOperation lo, List<String> targets)
	{
		super(ce);
		
		this.lo = lo;
		this.targets = targets;
	}

	public SJCompoundOperation node()
	{
		return lo;
	}
	
	/* (non-Javadoc)
	 * @see sessionj.types.contexts.SJSessionContext#targets()
	 */
	public List<String> targets()
	{
		return targets;
	}
}
