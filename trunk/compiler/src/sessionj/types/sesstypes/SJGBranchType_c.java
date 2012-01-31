//<By MQ> Added
package sessionj.types.sesstypes;

import java.util.*;

import polyglot.types.*;

import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;

public class SJGBranchType_c extends SJBranchType_c implements SJGBranchType
{
	public static final long serialVersionUID = SJ_VERSION;

	public SJGBranchType_c(TypeSystem ts)
	{
		this(ts, false);
	}

	public SJGBranchType_c(TypeSystem ts, boolean isDependentlyTyped)
	{
		super(ts, isDependentlyTyped);
	}
	
	public SJGBranchType branchCase(SJLabel lab, SJSessionType st)
	{
		return (SJGBranchType) super.branchCase(lab, st);
	}

    @Override
    protected SJBranchType skeleton(boolean dependentlyTyped) {
        return typeSystem().SJGBranchType(dependentlyTyped);
    }

    protected boolean eligibleForEquals(SJSessionType st)
	{
		return st instanceof SJGBranchType && labelSet().equals(((SJBranchType) st).labelSet());
	}
	
	protected boolean eligibleForSubtype(SJSessionType st)
	{
		return st instanceof SJGBranchType && ((SJBranchType) st).labelSet().containsAll(labelSet());
	}
	
	protected boolean eligibleForDualtype(SJSessionType st)
	{
	        return false; //st instanceof SJOutbranchType && labelSet().containsAll(((SJBranchType) st).labelSet());
	}
	
	public SJSessionType nodeSubsume(SJSessionType st) throws SemanticException
	{
		if (!(st instanceof SJGBranchType)) 
		{
			throw new SemanticException("[SJGBranchType_c] Not subsumable: " + this + ", " + st);
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
						throw new SemanticException("[SJGBranchType_c] Not subsumable: " + this + ", " + st);
					}
					
					res = res.branchCase(lab, null);
				}
				else
				{
					if (theirs == null)
					{
						throw new SemanticException("[SJGBranchType_c] Not subsumable: " + this + ", " + st);
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
		
		throw new RuntimeException("[SJGBranchType_c] Shouldn't get here: " + ourLabels + " " + op + " " + theirLabels);
	}	
	
	protected SJGBranchType skeleton() // FIXME: finish refactoring against the isDependentlyType versions.
	{
		return typeSystem().SJGBranchType();
	}
	
	protected String branchConstructorOpen()
	{
		return "{";
	}
	
	protected String branchConstructorClose()
	{
		return "{";
	}

    protected SJSessionType dualSkeleton() {
        return null; //MQTODO: typeSystem().SJOutbranchType();
    }
}
