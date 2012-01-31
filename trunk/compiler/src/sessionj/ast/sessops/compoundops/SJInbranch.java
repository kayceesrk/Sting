package sessionj.ast.sessops.compoundops;

import sessionj.ast.sessops.basicops.SJInlabel;

import java.util.List;

public interface SJInbranch extends SJBranchOperation
{
	List<SJInbranchCase> branchCases();
	SJInbranch branchCases(List<SJInbranchCase> branchCases);
	
	SJInlabel inlabel();
	SJInbranch inlabel(SJInlabel il);
}
