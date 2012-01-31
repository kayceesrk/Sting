package sessionj.types.sesstypes;

import polyglot.types.*;

import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;

public class SJRecurseType_c extends SJSessionType_c implements SJRecurseType
{
	public static final long serialVersionUID = SJ_VERSION;

	private SJLabel lab;

	public SJRecurseType_c(TypeSystem ts, SJLabel lab)
	{
		super(ts);

		this.lab = lab;
	}

	public SJLabel label()
	{
		return lab;
	}

	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJRecurseType && compareLabel((SJRecurseType) st);
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJRecurseType && compareLabel((SJRecurseType) st);
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJRecurseType && compareLabel((SJRecurseType) st);
	}
	
	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{
        return true; // Checking eligibleFor... is already enough.
	}

	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!st.startsWith(SJRecurseType.class) && compareLabel((SJRecurseType) st))
		{
			throw new SemanticException("[SJRecurseType_c] Not subsumable: " + this + ", " + st);
		}

		return typeSystem().SJRecurseType(label()); // SJLabel is immutable, no need to clone.
	}

	public boolean nodeWellFormed() // Well-formed recursions checked by external routine.
	{
		return true;
	}

	public SJSessionType nodeClone()
	{
		return typeSystem().SJRecurseType(label()); // SJLabel is immutable, no need to clone.
	}

	public String nodeToString()
	{
		return SJ_STRING_RECURSE_PREFIX + label().toString();
	}
	
	private boolean compareLabel(SJRecurseType rt)
	{
		return label().equals(rt.label());
	}

    public SJSessionType nodeDual() throws SemanticException {
        return typeSystem().SJRecurseType(label());
        // TODO: is this equivalent to copy() ? copy() would be clearer.
        // Or this, if we made everything immutable... One can dream
    }
}
