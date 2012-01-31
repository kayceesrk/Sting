package sessionj.types.sesstypes;

import polyglot.types.*;

import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;

public class SJRecursionType_c extends SJLoopType_c implements SJRecursionType
{
	private static final long serialVersionUID = SJ_VERSION;

	private SJLabel lab;

	public SJRecursionType_c(TypeSystem ts, SJLabel lab)
	{
		super(ts);

		this.lab = lab; // SJLabel is immutable, no need to clone.
	}

	public SJLabel label()
	{
		return lab;
	}
	
	public boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJRecursionType && compareLabel((SJRecursionType) st);
	}
	
	public boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJRecursionType && compareLabel((SJRecursionType) st);	
	}

	public boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJRecursionType && compareLabel((SJRecursionType) st);
	}

	protected SJRecursionType skeleton()
	{
		return typeSystem().SJRecursionType(label()); // SJLabel is immutable, no need to clone.
	}
	
	protected boolean eligibleForSubsume(SJSessionType st)
	{
		return st instanceof SJRecursionType && compareLabel((SJRecursionType) st);
	}
	
	protected String loopConstructorOpen()
	{
		return SJ_STRING_REC + ' ' + label() + SJ_STRING_RECURSION_OPEN;
	}
	
	protected String loopConstructorClose()
	{
		return SJ_STRING_RECURSION_CLOSE;
	}
	
	private boolean compareLabel(SJRecursionType rt)
	{
		return label().equals(rt.label()); 
	}

    protected SJLoopType dualSkeleton() {
        return typeSystem().SJRecursionType(label()); // TODO use copy instead? make sure same behaviour
    }
}
