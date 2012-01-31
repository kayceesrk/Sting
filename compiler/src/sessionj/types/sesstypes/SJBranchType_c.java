package sessionj.types.sesstypes;

import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import static sessionj.SJConstants.*;
import sessionj.util.SJCompilerUtils;
import sessionj.util.SJLabel;

import java.util.HashMap;
import java.util.Set;

abstract public class SJBranchType_c extends SJSessionType_c implements SJBranchType
{
	private static final long serialVersionUID = SJ_VERSION;
	
	private final HashMap<SJLabel, SJSessionType> cases = new HashMap<SJLabel, SJSessionType>();

	private final boolean isDependentlyTyped;

    protected SJBranchType_c(TypeSystem ts, boolean isDependentlyTyped)
	{
		super(ts);
		
		this.isDependentlyTyped = isDependentlyTyped;
	}
	
	public Set<SJLabel> labelSet()
	{
		return cases.keySet();
	}

	public boolean hasCase(SJLabel lab)
	{
		return labelSet().contains(lab);
	}

    public SJSessionType branchCase(SJLabel lab)
	{
        return cases.get(lab);
    }

	public SJBranchType branchCase(SJLabel lab, SJSessionType st) 
	{
		SJBranchType_c bt = (SJBranchType_c) nodeClone(); // Defensive copy.

        bt.cases.put(lab, st);

        bt.setChild(child());
		
		return bt;  
	}

    protected abstract SJBranchType skeleton(boolean dependentlyTyped);

    public SJSessionType removeCase(SJLabel lab)
	{
		return cases.remove(lab); // No need to copy here.
	}

	protected boolean compareNode(NodeComparison op, SJSessionType st)
	{	
		SJBranchType_c them = (SJBranchType_c) st;		
		Set<SJLabel> theirLabels = them.labelSet(); 		
		
		// Loops on labels, but returns as soon as the matching label on the
        // counterpart is found. Hence, the loops won't go through all labels.
        for (SJLabel lab : selectComparsionLabelSet(labelSet(), theirLabels, op))
		{
            SJSessionType case1 = cases.get(lab);
            SJSessionType case2 = them.cases.get(lab);
	
			if (case1 == null)
			{
				if (case2 != null) return false;
			}
			else
			{
                return op.apply(case1, case2);
			}
		}						
		
		throw new RuntimeException("[SJBranchType_c] Shouldn't get here: " + op);
	}	

	public boolean nodeWellFormed()
	{
		if (labelSet().isEmpty())
		{
			return false;
		}

		for (SJLabel lab : labelSet())
		{
            SJSessionType st = cases.get(lab);

			if (st != null && !st.treeWellFormed())
			{
				return false;
			}
		}

		return true;
	}

	public SJSessionType nodeClone()
	{
		SJBranchType bt = skeleton(isDependentlyTyped);
		
		/*// This copies the branch cases.
		for (SJLabel lab : labelSet()) 
		{
			bt = bt.branchCase(lab, getBranchCase(lab)); // null or else cloned by setter method.
		}*/
		
        ((SJBranchType_c) bt).cases.putAll(cases); // Safe not to copy the branch cases. This would be true for the child (for all SJSessionType) as well.
		return bt;
	}

    private static final int TRIM_LEN = SJ_STRING_CASE_SEPARATOR.length() + 1;
	public String nodeToString()
	{
		StringBuilder s = new StringBuilder(branchConstructorOpen());

        for (SJLabel lab : labelSet()) {
            SJSessionType branchCase = cases.get(lab);

            s.append(lab).append(SJ_STRING_LABEL);
            s.append(branchCase == null ? " " : branchCase.toString());

            s.append(SJ_STRING_CASE_SEPARATOR).append(' ');
        }
        s.delete(s.length()-TRIM_LEN, s.length()); // Deletes the constructor prefix if the branch cases are empty (it shouldn't be empty, but this messes up error printing when it is).

		return s.append(branchConstructorClose()).toString();
	}

	abstract protected Set<SJLabel> selectComparsionLabelSet(Set<SJLabel> ourLabels, Set<SJLabel> theirLabels, NodeComparison op);
	
	abstract protected SJBranchType skeleton();
	
	abstract protected String branchConstructorOpen();
	abstract protected String branchConstructorClose();
    protected abstract SJSessionType dualSkeleton();
    public SJSessionType nodeDual() throws SemanticException {
        SJSessionType dual = dualSkeleton();
        for (SJLabel lab : labelSet())
        {
            dual = ((SJBranchType) dual).branchCase
                (lab, SJCompilerUtils.dualSessionType(branchCase(lab)));
        }
        return dual;
    }
    
  public final boolean isDependentlyTyped()
  {
  	return isDependentlyTyped;
  }
}
