package sessionj.ast.sessops.compoundops;

import java.util.List;

import polyglot.ast.*;

import sessionj.ast.sessops.*;

/**
 * 
 * @author Raymond
 *
 */
public interface SJBranchOperation extends SJCompoundOperation
{	
	/*public List<SJInbranchCase> branchCases(); // Not needed by SJOutbranch. But could make SJOutbranch not a SJBranchOperation (it's also an SJBranchCase).
	public SJInbranch branchCases(List<SJInbranchCase> branchCases);*/	
	
	public boolean isDependentlyTyped();
}
