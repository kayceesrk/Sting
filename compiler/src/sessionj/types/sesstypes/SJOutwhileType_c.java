package sessionj.types.sesstypes;

import polyglot.types.*;

import static sessionj.SJConstants.*;

public class SJOutwhileType_c extends SJLoopType_c implements SJOutwhileType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJOutwhileType_c(TypeSystem ts) // Could make a constructor to take the body.
	{
		super(ts);
	}

	public SJOutwhileType body(SJSessionType body)
	{
		return (SJOutwhileType) super.body(body);
	}
	
	public boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJOutwhileType;
	}
	
	public boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJOutwhileType;	
	}

	public boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJInwhileType;
	}
	
	protected SJOutwhileType skeleton()
	{
		return typeSystem().SJOutwhileType();
	}
	
	protected boolean eligibleForSubsume(SJSessionType st)
	{
		return st instanceof SJOutwhileType;
	}
	
	protected String loopConstructorOpen()
	{
		return SJ_STRING_OUTWHILE_OPEN;
	}
	
	protected String loopConstructorClose()
	{
		return SJ_STRING_OUTWHILE_CLOSE;
	}

    protected SJLoopType dualSkeleton() {
        return typeSystem().SJInwhileType("MQUnknown"); //<By MQ>
    }

    @Override
    public boolean startsWithOutput() {
        return true;
    }
}
