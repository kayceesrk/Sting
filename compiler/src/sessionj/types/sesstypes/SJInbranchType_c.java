package sessionj.types.sesstypes;

import java.util.*;

import polyglot.types.*;

import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;

public class SJInbranchType_c extends SJBranchType_c implements SJInbranchType
{
	public static final long serialVersionUID = SJ_VERSION;
    //<By MQ>
    protected String target;
    public String target()
    {
        return target;
    }
    public void target(String target)
    {
        this.target = target;
    }
    //</By MQ>
        public SJInbranchType_c(TypeSystem ts, String target) //<By MQ> Added target
	{
	        this(ts, target, false); //<By MQ> Added target
		this.target = target; //<By MQ> Added target
	}

        public SJInbranchType_c(TypeSystem ts, String target, boolean isDependentlyTyped) //<By MQ> Added target
	{
		super(ts, isDependentlyTyped);
		this.target = target; //<By MQ> Added target
	}
	
	public SJInbranchType branchCase(SJLabel lab, SJSessionType st)
	{
		return (SJInbranchType) super.branchCase(lab, st);
	}

    @Override
    protected SJBranchType skeleton(boolean dependentlyTyped) {
        return typeSystem().SJInbranchType(target, dependentlyTyped); //<By MQ> Added target
    }

    protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJInbranchType && labelSet().equals(((SJBranchType) st).labelSet());
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJInbranchType && ((SJBranchType) st).labelSet().containsAll(labelSet());
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
		return st instanceof SJOutbranchType && labelSet().containsAll(((SJBranchType) st).labelSet());
	}
	
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJInbranchType)) 
		{
			throw new SemanticException("[SJInbranchType_c] Not subsumable: " + this + ", " + st);
		}
		
		SJBranchType them = (SJBranchType) st;
		SJBranchType res = skeleton();

		for (SJLabel lab : labelSet())
		{
			if (them.hasCase(lab))
			{
				SJSessionType ours = branchCase(lab);
				SJSessionType theirs = them.branchCase(lab);
				
				if (ours == null)
				{
					if (theirs != null)
					{
						throw new SemanticException("[SJInbranchType_c] Not subsumable: " + this + ", " + st);
					}
					
					res = res.branchCase(lab, null);
				}
				else
				{
					if (theirs == null)
					{
						throw new SemanticException("[SJInbranchType_c] Not subsumable: " + this + ", " + st);
					}
					
					res = res.branchCase(lab, ours.subsume(theirs));
				}
			}
		}

		return res;
	}
	
	protected Set<SJLabel> selectComparsionLabelSet(Set<SJLabel> ourLabels, Set<SJLabel> theirLabels, NodeComparison op)  
	{
		switch (op)
		{
			case EQUALS: return ourLabels;
			case SUBTYPE: return ourLabels; // The requirements of a sent inbranch are met by an implementation that can receive a bigger inbranch.
			case DUALTYPE: return theirLabels;
		}
		
		throw new RuntimeException("[SJInbranchType_c] Shouldn't get here: " + ourLabels + " " + op + " " + theirLabels);
	}	
	
	protected SJInbranchType skeleton() // FIXME: finish refactoring against the isDependentlyType versions.
	{
	    return typeSystem().SJInbranchType(target);//<By MQ> Added target
	}
	
	protected String branchConstructorOpen()
	{
	    return target + ":" + SJ_STRING_INBRANCH_OPEN; //<By MQ> Added target
	}
	
	protected String branchConstructorClose()
	{
		return SJ_STRING_INBRANCH_CLOSE;
	}

    protected SJSessionType dualSkeleton() {
        return typeSystem().SJOutbranchType();
    }
}
