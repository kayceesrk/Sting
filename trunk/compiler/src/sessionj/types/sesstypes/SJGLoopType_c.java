//<By MQ> Added
package sessionj.types.sesstypes;

import polyglot.types.*;

import static sessionj.SJConstants.*;

public class SJGLoopType_c extends SJLoopType_c implements SJGLoopType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJGLoopType_c(TypeSystem ts)
	{
		super(ts);
	}

	public SJGLoopType body(SJSessionType body)
	{
		return (SJGLoopType) super.body(body);
	}
	
	protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJGLoopType;
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJGLoopType;
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
	        return false; //st instanceof SJOutwhileType;
	}
	
	protected SJGLoopType skeleton()
	{
		return typeSystem().SJGLoopType();
	}

    @Override
    protected SJLoopType dualSkeleton() {
        return null; //typeSystem().SJOutwhileType();
    }

    protected boolean eligibleForSubsume(SJSessionType st)
	{
		return st instanceof SJGLoopType;
	}
	
	protected String loopConstructorOpen()
	{
		return "[";
	}
	
	protected String loopConstructorClose()
	{
		return "]";
	}	
}
