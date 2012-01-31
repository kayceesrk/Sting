package sessionj.runtime.session.contexts;

import sessionj.types.contexts.SJTypeBuildingContext_c;
import sessionj.types.sesstypes.SJRecursionType;
import sessionj.types.sesstypes.SJSessionType;
import sessionj.types.sesstypes.SJLoopType;
import sessionj.util.SJLabel;

import java.util.Map;

public class SJRecursionContext extends SJLoopContext // Analogous to the compiler contexts.
{
	private final SJLabel lab;

	private final SJSessionType originalBody;

	public SJRecursionContext(SJRecursionType rt, Map<SJLabel, SJRecursionType> recVarsInScope)
	{
		super(unroll(rt, recVarsInScope)); 

        lab = rt.label();
        originalBody = unroll(rt, recVarsInScope);
	}

	private static SJSessionType unroll(SJLoopType rt, Map<SJLabel, SJRecursionType> recVarsInScope) {
		return SJTypeBuildingContext_c.substituteTypeVariables(rt.body(), recVarsInScope); 
	}

	public SJLabel label()
	{
		return lab;
	}

	/*
	public SJRecursionType originalType()
	{
		return original;
	}
	*/
	
	public SJRecursionContext withOriginalBody() {
		return (SJRecursionContext) activeType(originalBody);
	}
	
    public boolean hasLabel(SJLabel lab) {
        return lab.equals(this.lab);
    }
}
