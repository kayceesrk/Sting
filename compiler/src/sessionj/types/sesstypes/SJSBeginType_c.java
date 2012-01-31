package sessionj.types.sesstypes;

import polyglot.types.*;
import sessionj.types.sesstypes.SJSessionType_c.NodeComparison;

import static sessionj.SJConstants.*;

public class SJSBeginType_c extends SJBeginType_c implements SJSBeginType
{
	public static final long serialVersionUID = SJ_VERSION;
    //<By MQ> Added target
    protected String target;
    public String target()
    {
	return target;
    }
    public SJSBeginType_c(TypeSystem ts, String target)
	{
		super(ts);
	        this.target = target;
	}
    //</By MQ>
	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJSBeginType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJSBeginType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJCBeginType;
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{
		return true; // Checking eligibleFor... is already enough.	
	}

	// Could refine the return types for this and nodeClone, but not very useful.
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJSBeginType))
		{
			throw new SemanticException("[SJSBeginType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJSBeginType(target); //<By MQ>
	}
	
	public SJSessionType nodeClone()
	{
	    return typeSystem().SJSBeginType(target); //<By MQ>
	}

	public String nodeToString()
	{
	    return target + ":" + SJ_STRING_SBEGIN; //<By MQ>
	}

    public SJSessionType nodeDual() {
        return typeSystem().SJCBeginType();
    }
}
