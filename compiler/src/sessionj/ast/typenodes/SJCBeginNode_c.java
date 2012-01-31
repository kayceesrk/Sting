package sessionj.ast.typenodes;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;

public class SJCBeginNode_c extends SJBeginNode_c implements SJCBeginNode
{
	public SJCBeginNode_c(Position pos)
	{
		super(pos);
	}

	// Duplicated from corresponding SJSessionType implementations. But may be needed before types have been built.
	public String nodeToString()
	{
		return SJ_STRING_CBEGIN;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) {
        return type(sjts.SJCBeginType());
    }
}
