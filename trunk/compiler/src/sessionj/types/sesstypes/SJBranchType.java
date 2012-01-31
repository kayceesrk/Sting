package sessionj.types.sesstypes;

import java.util.Set;

import sessionj.util.SJLabel;

public interface SJBranchType extends SJSessionType
{
	public Set<SJLabel> labelSet();
	public boolean hasCase(SJLabel lab);
	public SJSessionType branchCase(SJLabel lab);
	public SJBranchType branchCase(SJLabel lab, SJSessionType st);
	public SJSessionType removeCase(SJLabel lab);
	
	public boolean isDependentlyTyped(); // Extended to support this in a backwards-compatible way.
}
