package sessionj.ast.typenodes;

import polyglot.types.SemanticException;
import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.util.SJCompilerUtils;

abstract public class SJLoopNode_c extends SJTypeNode_c implements SJLoopNode
{
	private SJTypeNode body;

	public SJLoopNode_c(Position pos, SJTypeNode body)
	{
		super(pos);

		this.body = body;
	}

	public SJTypeNode body()
	{
		return body;
	}

	public SJLoopNode body(SJTypeNode body)
	{
		this.body = body;

		return this;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) throws SemanticException {
        SJSessionType bt = null;
        if (body != null) {
            bt = SJCompilerUtils.disambiguateSJTypeNode(cv, body).type();
        }
        return type(createType(bt, sjts));
    }

    protected abstract SJSessionType createType(SJSessionType bodyType, SJTypeSystem ts);
}
