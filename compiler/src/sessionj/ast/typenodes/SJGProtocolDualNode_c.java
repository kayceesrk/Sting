//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.ast.Receiver;
import polyglot.util.Position;

import static sessionj.SJConstants.*;

public class SJGProtocolDualNode_c extends SJGProtocolNode_c implements SJGProtocolDualNode
{
	public SJGProtocolDualNode_c(Position pos, Receiver target)
	{
		super(pos, target);
	}

	public String nodeToString()
	{
		return SJ_STRING_GPROTOCOL_DUAL_PREFIX + "(" + target() + ")"; // Factor out constants.
	}
}
//</By MQ>