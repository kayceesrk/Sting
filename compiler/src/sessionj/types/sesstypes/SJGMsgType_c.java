//<By MQ> Added
package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import static sessionj.SJConstants.*;
import sessionj.util.SJCompilerUtils;

public class SJGMsgType_c extends SJMessageCommunicationType_c implements SJGMsgType
{
	public static final long serialVersionUID = SJ_VERSION;

	protected SJGMsgType_c(TypeSystem ts)
	{
		super(ts);
	}
	
	public SJGMsgType_c(TypeSystem ts, Type messageType) throws SemanticException
	{
	    super(ts,null, messageType); 
	}
	
	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJGMsgType;
	}

	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJGMsgType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
	        return false; //st instanceof SJReceiveType;
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{	
		switch (op)
		{
			case EQUALS: return messageType().equals(((SJGMsgType) st).messageType()); 
			case SUBTYPE: return ((SJGMsgType) st).messageType().isSubtype(messageType()); // Contravariant.  
		        case DUALTYPE: return false; //messageType().isSubtype(((SJReceiveType) st).messageType());
		}
		
		throw new RuntimeException("[SJGMsgType_c] Shouldn't get here: " + op);
	}
	
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJGMsgType))
		{
			throw new SemanticException("[SJGMsgType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJGMsgType(subsumeGMsgTypes(messageType(), ((SJGMsgType) st).messageType()));
	}

	protected SJGMsgType skeleton()
	{
		return new SJGMsgType_c(typeSystem());
	}
	
	protected String messageCommunicationOpen()
	{
		return "<";
	}
	
	protected String messageCommunicationClose()
	{
		return ">";
	}

    public SJSessionType nodeDual() throws SemanticException {
        return null; //MQTODO
    }

    public static Type subsumeGMsgTypes(Type t1, Type t2) throws SemanticException
    {
        return SJCompilerUtils.subsumeMessageTypes(t1, t2, true);
    }

    @Override
    public boolean startsWithOutput() {
        return true;
    }
}
