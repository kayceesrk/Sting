package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import static sessionj.SJConstants.SJ_STRING_GBEGIN;
import static sessionj.SJConstants.SJ_VERSION;

public class SJGBeginType_c extends SJBeginType_c implements SJGBeginType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJGBeginType_c(TypeSystem ts)
	{
		super(ts);
	}

	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJGBeginType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJGBeginType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
	        return false; //st instanceof SJSBeginType;
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{
		return true; // Checking eligibleFor... is already enough.	
	}
	
	// Could refine the return types for this and nodeClone, but not very useful.
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJGBeginType))
		{
			throw new SemanticException("[SJGBeginType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJGBeginType();
	}

	public SJSessionType nodeClone()
	{
		return typeSystem().SJGBeginType();
	}

	public String nodeToString()
	{
		return SJ_STRING_GBEGIN;
	}

    public SJSessionType nodeDual() {
        return null; //MQTODO typeSystem().SJSBeginType();
    }
}
