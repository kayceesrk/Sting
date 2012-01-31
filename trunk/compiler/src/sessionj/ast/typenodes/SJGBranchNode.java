//<By MQ> Added
package sessionj.ast.typenodes;

import java.util.List;

public interface SJGBranchNode extends SJBranchNode
{
	public SJGBranchNode branchCases(List<SJBranchCaseNode> branchCases);
}
