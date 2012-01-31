package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import static sessionj.SJConstants.*;
import sessionj.util.SJCompilerUtils;
import polyglot.ast.Id; //<By MQ>

public class SJReceiveType_c extends SJMessageCommunicationType_c implements SJReceiveType
{
	private static final long serialVersionUID = SJ_VERSION;
	protected SJReceiveType_c(TypeSystem ts)
	{
		super(ts);
	}
	
        public SJReceiveType_c(TypeSystem ts, String target,  Type messageType) throws SemanticException  //<By MQ>
	{
	    super(ts, target, messageType); //<By MQ>
	}

	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJReceiveType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJReceiveType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJSendType;
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{
		switch (op)
		{
			case EQUALS: return messageType().typeEquals(((SJMessageCommunicationType) st).messageType()); 
			case SUBTYPE: return messageType().isSubtype(((SJMessageCommunicationType) st).messageType());
			case DUALTYPE: return ((SJMessageCommunicationType) st).messageType().isSubtype(messageType());
		}
		
		throw new RuntimeException("[SJReceiveType_c] Shouldn't get here: " + op);
	}

	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJReceiveType))
		{
			throw new SemanticException("[SJReceiveType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJReceiveType(target, subsumeReceiveTypes(messageType(), ((SJReceiveType) st).messageType())); //<By MQ>
	}

	protected SJReceiveType skeleton()
	{
		return new SJReceiveType_c(typeSystem());
	}
	
	protected String messageCommunicationOpen()
	{
	        return SJ_STRING_RECEIVE_OPEN;  //<By MQ>
	}
	
	protected String messageCommunicationClose()
	{
		return SJ_STRING_RECEIVE_CLOSE;
	}

    public SJSessionType nodeDual() throws SemanticException {
        return typeSystem().SJSendType(target, messageType()); //<By MQ> MQTODO: shouldn't the target be MY ID instead of their's?
    }

    public static Type subsumeReceiveTypes(Type t1, Type t2) throws SemanticException
    {
        return SJCompilerUtils.subsumeMessageTypes(t1, t2, false);
    }
}
