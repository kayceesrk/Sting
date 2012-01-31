//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;

public class SJGBeginNode_c extends SJBeginNode_c implements SJGBeginNode
{
    String target;
    public String target()
    {
	return target;
    }
    public SJGBeginNode_c(Position pos, String target)
	{
		super(pos);
		this.target = target;
	}

	// Duplicated from corresponding SJSessionType implementations. But may be needed before types have been built.
	public String nodeToString()
	{
		return target + ":" + SJ_STRING_GBEGIN;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) {
        return type(sjts.SJGBeginType());
    }
}
