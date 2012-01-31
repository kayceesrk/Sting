package sessionj.ast.typenodes;

import polyglot.ast.*;
import polyglot.util.Position;

import static sessionj.SJConstants.*;

public class SJProtocolRefNode_c extends SJProtocolNode_c implements SJProtocolRefNode
{
	public SJProtocolRefNode_c(Position pos, Receiver target)
	{
		super(pos, target);
	}

	public String nodeToString()
	{
		return SJ_STRING_PROTOCOL_REF_PREFIX + "(" + target() + ")"; // Factor out constants.
	}
}
