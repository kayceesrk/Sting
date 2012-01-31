package sessionj.types.sesstypes;

import polyglot.types.*;

import static sessionj.SJConstants.*;

public class SJInwhileType_c extends SJLoopType_c implements SJInwhileType
{
    //<By MQ>
        private String target;
        public String target()
        {
	    return target;
	}
	public static final long serialVersionUID = SJ_VERSION;

        public SJInwhileType_c(TypeSystem ts, String target)
	{
		super(ts);
		this.target = target;
	}
    //</By MQ>
	public SJInwhileType body(SJSessionType body)
	{
		return (SJInwhileType) super.body(body);
	}
	
	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJInwhileType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJInwhileType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJOutwhileType;
	}
	
	protected SJInwhileType skeleton()
	{
	        return typeSystem().SJInwhileType(target); //<By MQ>
	}

    @Override
    protected SJLoopType dualSkeleton() {
        return typeSystem().SJOutwhileType();
    }

    protected boolean eligibleForSubsume(SJSessionType st)
	{
		return st instanceof SJInwhileType;
	}
	
	protected String loopConstructorOpen()
	{
	        return target + ":" + SJ_STRING_INWHILE_OPEN; //<By MQ>
	}
	
	protected String loopConstructorClose()
	{
		return SJ_STRING_INWHILE_CLOSE;
	}	
}
