package sessionj.ast.typenodes;

import java.util.List;

public interface SJOutbranchNode extends SJBranchNode
{
	public SJOutbranchNode branchCases(List<SJBranchCaseNode> branchCases);
}
