package sessionj.ast.typenodes;

import polyglot.ast.TypeNode;
import polyglot.util.Position;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.visit.ContextVisitor;
import polyglot.frontend.Job;
import java.util.List; //<By MQ>
import polyglot.ast.Id; //<By MQ>
import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJMessageCommunicationType;
import sessionj.util.SJCompilerUtils;

public class SJReceiveNode_c extends SJMessageCommunicationNode_c implements SJReceiveNode
{
        //<By MQ> Changed the 2nd argument of SJReceiveNode_c from TypeNode to List in order to support batches of messages
        private String target;
        public SJReceiveNode_c(Position pos, String target, TypeNode/*List*/ messageType)
	{
	    super(pos, messageType/*null*/); //MQTODO: the null parameter must be substituted. This paramter should pass messageType
	    this.target = target;
	}
    public String target()
    {
	return target;
    }
    //</By MQ>
    protected SJMessageCommunicationType createType(SJTypeSystem ts, Type messageType) throws SemanticException {
        return ts.SJReceiveType(target, messageType); //<By MQ>
    }

    public String nodeToString()
	{
		String message = messageType().toString(); // toString enough for messageType? or need to manually get full name?

		return target + ":" + SJ_STRING_RECEIVE_OPEN + message + SJ_STRING_RECEIVE_CLOSE; //<By MQ>
	}
}
