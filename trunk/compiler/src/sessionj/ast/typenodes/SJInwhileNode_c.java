package sessionj.ast.typenodes;

import polyglot.util.Position;

import static sessionj.SJConstants.*;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.SJTypeSystem;
import polyglot.ast.Id; //<By MQ>

public class SJInwhileNode_c extends SJLoopNode_c implements SJInwhileNode
{
        //<By MQ>
        private String target;
        public SJInwhileNode_c(Position pos, String target, SJTypeNode body)
	{
		super(pos, body);
		this.target = target;
	}
        public String target()
        { 
	    return target;
	}
        //</By MQ>
    @Override
    protected SJSessionType createType(SJSessionType bodyType, SJTypeSystem ts) {
        return ts.SJInwhileType(bodyType, target); //<By MQ>
    }

    public String nodeToString()
	{
	        String m = target + ":" + SJ_STRING_INWHILE_OPEN; //<By MQ>
		
		if (body() != null)
		{
			m += body().toString();
		}
		
		return m + SJ_STRING_INWHILE_CLOSE;
	}
}
