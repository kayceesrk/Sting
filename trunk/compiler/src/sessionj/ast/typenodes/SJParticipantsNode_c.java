//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;

import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;
import java.util.*;
import polyglot.ast.Id;

public class SJParticipantsNode_c extends SJTypeNode_c implements SJParticipantsNode
{
        private List<Id> IDs;
        public SJParticipantsNode_c(Position pos, List<Id> IDs)
	{
		super(pos);
		this.IDs = IDs;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) {
        return type(sjts.SJParticipantsType());
    }
    public String nodeToString()
    {
	String s = SJ_STRING_PARTICIPANTS + ":";
	int k = 0;
	for(Id id : IDs)
	{
	    if(k++ != 0)
		s += " | ";
	    s += id;
	}
	return s;
    }
    @Override public String toString()
    {
	return nodeToString();
    }

}
//</By MQ>