package sessionj.ast.typenodes;

import polyglot.ast.TypeNode;
import polyglot.ast.Id; //<By MQ>
import polyglot.util.Position;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import java.util.List; //<By MQ>
import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJMessageCommunicationType;

public class SJSendNode_c extends SJMessageCommunicationNode_c implements SJSendNode
{
        private String target; //<By MQ>
        //<By MQ> Changed the 2nd argument of SJSendNode_c from TypeNode to List in order to support batches of messages
        public SJSendNode_c(Position pos, String target, TypeNode/*List*/ messageType)
	{
	    super(pos, messageType/*null*/);  //MQTODO: the null parameter must be substituted. This paramter should pass messageType
	    this.target = target;
	}
        public String target()
        {
	    return target;
	}

	//</By MQ>
    protected SJMessageCommunicationType createType(SJTypeSystem ts, String target, Type messageType) throws SemanticException {
        return ts.SJSendType(target, messageType);
    }
    protected SJMessageCommunicationType createType(SJTypeSystem ts, Type messageType) throws SemanticException {  //<By MQ> Kept only for compatibility with base class. BAD DESIGN
        return ts.SJSendType(target, messageType);
    }

    public String nodeToString()
	{
		String message = messageType().toString(); // toString enough for messageType? or need to manually get full name?

		return target + ":" + SJ_STRING_SEND_OPEN + message + SJ_STRING_SEND_CLOSE;  //<By MQ> added target.
	}
}
