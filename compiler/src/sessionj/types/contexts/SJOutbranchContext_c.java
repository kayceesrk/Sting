/**
 * 
 */
package sessionj.types.contexts;

import sessionj.ast.sessops.compoundops.SJOutbranch;
import sessionj.util.SJLabel;

import java.util.List;

/**
 * @author Raymond
 *
 */
public class SJOutbranchContext_c extends SJBranchCaseContext_c implements SJOutbranchContext
{
	private SJOutbranch ob; // Basically duplicated from SJSessionContext_c.	
	private List<String> targets;	
	
	public SJOutbranchContext_c()
	{
		
	}
	
	public SJOutbranchContext_c(SJContextElement ce, SJOutbranch ob, List<String> targets, SJLabel lab)
	{
		super(ce, lab);	
		
		this.targets = targets;
		this.ob = ob;
	}
	
	public SJOutbranch node()
	{
		return ob;
	}
	
	/* (non-Javadoc)
	 * @see sessionj.types.contexts.SJSessionContext#targets()
	 */
	public List<String> targets()
	{
		return targets;
	}	
}
