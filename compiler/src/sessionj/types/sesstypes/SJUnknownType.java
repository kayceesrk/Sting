package sessionj.types.sesstypes;

import polyglot.types.*;

import static sessionj.SJConstants.*;

public class SJUnknownType extends SJSessionType_c implements SJSessionType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJUnknownType(TypeSystem ts)
	{
		super(ts);
	}

	// Move default node methods to SJSessionType_c? Are these suitable default values?
	public boolean nodeEquals(SJSessionType st)
	{
		return false;
	}

	protected boolean eligibleForEquals(SJSessionType st)
	{
		return false;
	}

	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return false;
	}

	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return false;
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{
		return false;
	}
	
	public boolean nodeWellFormed()
	{
		return true; // FIXME: should be false? Or needed (hacked) for some use of session types before disambiguation is completed? 
	}

	public SJSessionType nodeClone()
	{
		return typeSystem().SJUnknownType();
	}

	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJUnknownType))
		{
			throw new SemanticException("[SJUnknownType] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJUnknownType();
	}

	public String nodeToString()
	{
		return SJ_STRING_UNKNOWN_TYPE;
	}
}
