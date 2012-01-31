package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import static sessionj.SJConstants.SJ_STRING_CBEGIN;
import static sessionj.SJConstants.SJ_VERSION;

public class SJCBeginType_c extends SJBeginType_c implements SJCBeginType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJCBeginType_c(TypeSystem ts)
	{
		super(ts);
	}

	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJCBeginType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJCBeginType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJSBeginType;
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{
		return true; // Checking eligibleFor... is already enough.	
	}
	
	// Could refine the return types for this and nodeClone, but not very useful.
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJCBeginType))
		{
			throw new SemanticException("[SJCBeginType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJCBeginType();
	}

	public SJSessionType nodeClone()
	{
		return typeSystem().SJCBeginType();
	}

	public String nodeToString()
	{
		return SJ_STRING_CBEGIN;
	}

    public SJSessionType nodeDual() {
        return typeSystem().SJSBeginType("MQUnknown");//<By MQ> MQTODO: what should we do to produce a valid dual?
    }
}
