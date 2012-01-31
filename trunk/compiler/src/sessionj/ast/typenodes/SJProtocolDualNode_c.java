package sessionj.ast.typenodes;

import polyglot.ast.Receiver;
import polyglot.util.Position;

import static sessionj.SJConstants.*;

public class SJProtocolDualNode_c extends SJProtocolNode_c implements SJProtocolDualNode
{
	public SJProtocolDualNode_c(Position pos, Receiver target)
	{
		super(pos, target);
	}

	public String nodeToString()
	{
		return SJ_STRING_PROTOCOL_DUAL_PREFIX + "(" + target() + ")"; // Factor out constants.
	}
}
