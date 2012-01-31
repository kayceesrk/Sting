//<By MQ> Added
package sessionj.ast.typenodes;

import polyglot.ast.TypeNode;
import polyglot.util.Position;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import java.util.List;
import static sessionj.SJConstants.*;
import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJMessageCommunicationType;

public class SJGMsgNode_c extends SJMessageCommunicationNode_c implements SJGMsgNode
{
    public String source;
    public String target;
    public SJGMsgNode_c(Position pos, String source, String target, TypeNode/*List*/ messageType)
	{
	    super(pos, messageType);  //MQTODO: the null parameter must be substituted. This paramter should pass messageType
	    this.source = source;
	    this.target = target;
	}

    protected SJMessageCommunicationType createType(SJTypeSystem ts, Type messageType) throws SemanticException {
        return ts.SJGMsgType(messageType);
    }

    public String nodeToString()
	{
		String message = messageType().toString(); // toString enough for messageType? or need to manually get full name?

		return source + "->" + target + ":<" + message + ">";
	}
}
