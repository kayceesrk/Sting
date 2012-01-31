package sessionj.ast.typenodes;

import sessionj.util.SJLabel;

public interface SJRecursionNode extends SJLoopNode
{
	public SJLabel label(); // Label value immutable.
}
