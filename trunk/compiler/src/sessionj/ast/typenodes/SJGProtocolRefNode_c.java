//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.ast.*;
import polyglot.util.Position;

import static sessionj.SJConstants.*;

public class SJGProtocolRefNode_c extends SJGProtocolNode_c implements SJGProtocolRefNode
{
	public SJGProtocolRefNode_c(Position pos, Receiver target)
	{
		super(pos, target);
	}

	public String nodeToString()
	{
		return SJ_STRING_GPROTOCOL_REF_PREFIX + "(" + target() + ")"; // Factor out constants.
	}
}
//</By MQ>