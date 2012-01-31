//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.util.Position;

import static sessionj.SJConstants.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.SJTypeSystem;

public class SJGLoopNode_c extends SJLoopNode_c implements SJGLoopNode
{
    public String guard;
    public SJGLoopNode_c(Position pos, String guard, SJTypeNode body)
	{
		super(pos, body);
		this.guard = guard;
	}

    @Override
    protected SJSessionType createType(SJSessionType bodyType, SJTypeSystem ts) {
        return ts.SJGLoopType(bodyType);
    }

    public String nodeToString()
	{
		String m = guard + ":[";
		
		if (body() != null)
		{
			m += body().toString();
		}
		
		return m + "]*";
	}
}
