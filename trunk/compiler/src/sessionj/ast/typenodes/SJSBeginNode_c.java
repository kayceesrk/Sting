package sessionj.ast.typenodes;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;

public class SJSBeginNode_c extends SJBeginNode_c implements SJSBeginNode
{
    //<By MQ> Adding Id
    protected String target;
    public String target()
    {
	return target;
    }
    public SJSBeginNode_c(Position pos, String target)
	{
		super(pos);
		this.target = target;
	}
    //</By MQ>
	// Duplicated from corresponding SJSessionType implementations. But may be needed before types have been built.
	public String nodeToString()
	{
	        return target + ":" + SJ_STRING_SBEGIN; //<By MQ>
	}

    public SJBeginNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) {
        return (SJBeginNode) type(sjts.SJSBeginType(target)); //<By MQ>
    }
}
