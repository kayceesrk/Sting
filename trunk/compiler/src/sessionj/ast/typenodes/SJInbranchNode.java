package sessionj.ast.typenodes;

import java.util.List;

public interface SJInbranchNode extends SJBranchNode
{
	public SJInbranchNode branchCases(List<SJBranchCaseNode> branchCases);
}
