package sessionj.ast.typenodes;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import static sessionj.SJConstants.SJ_STRING_RECURSE_PREFIX;
import sessionj.types.SJTypeSystem;
import sessionj.util.SJLabel;

public class SJRecurseNode_c extends SJTypeNode_c implements SJRecurseNode
{
	private final SJLabel lab;

	public SJRecurseNode_c(Position pos, SJLabel lab)
	{
		super(pos);

		this.lab = lab;
	}

	public SJLabel label()
	{
		return lab;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) {
        return (SJTypeNode) type(sjts.SJRecurseType(label()));
    }

    public String nodeToString()
	{
		return SJ_STRING_RECURSE_PREFIX + label();
	}	
}
