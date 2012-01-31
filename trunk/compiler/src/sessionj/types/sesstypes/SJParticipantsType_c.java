//<By MQ> Added
package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import static sessionj.SJConstants.SJ_STRING_PARTICIPANTS;
import static sessionj.SJConstants.SJ_VERSION;

public class SJParticipantsType_c extends SJSessionType_c implements SJParticipantsType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJParticipantsType_c(TypeSystem ts)
	{
		super(ts);
	}

	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJParticipantsType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJParticipantsType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return false;
	}
        protected boolean compareNode(NodeComparison op, SJSessionType st)
        {
	    return true; // Checking eligibleFor... is already enough.
	}
    public boolean nodeWellFormed()
    {
	return true; // MQTODO: should be false? Or needed (hacked) for some use of session types before disambiguation is completed?
    }

	
	// Could refine the return types for this and nodeClone, but not very useful.
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJParticipantsType))
		{
			throw new SemanticException("[SJParticipantsType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJParticipantsType();
	}

	public SJSessionType nodeClone()
	{
		return typeSystem().SJParticipantsType();
	}

	public String nodeToString()
	{
		return SJ_STRING_PARTICIPANTS;
	}

    public SJSessionType nodeDual() {
        return null; //MQTODO
    }
}
