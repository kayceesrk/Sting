/**
 * 
 */
package sessionj.types.contexts;

import java.util.*;

import sessionj.types.sesstypes.*;
import sessionj.types.typeobjects.*;

/**
 * @author Raymond
 *
 */
public class SJBranchContext_c extends SJContextElement_c implements SJBranchContext
{
	private List<SJContextElement> branches = new LinkedList<SJContextElement>();
	
	private boolean hasSessionImplementations = false;
	
	public SJBranchContext_c()
	{
		
	}
	
	public SJBranchContext_c(SJContextElement ce)
	{
		super(ce);
	}

	public void addBranch(SJContextElement ce)
	{
		branches.add(ce);
	}
	
	public List<SJContextElement> branches()
	{
		return branches;
	}
	
	public boolean hasSessionImplementations()
	{
		return hasSessionImplementations;
	}
	
	public void setHasSessionImplementations(boolean hasSessionImplementations)
	{
		this.hasSessionImplementations = hasSessionImplementations;
	}
}
