package sessionj.ast.sessops.compoundops;

import polyglot.ast.Block_c;
import polyglot.ast.Receiver;
import polyglot.util.Position;
import sessionj.util.SJLabel;
import sessionj.ast.sessvars.SJVariable;
import sessionj.ast.sessops.SJSessionOperation;

import java.util.List;

public class SJOutbranch_c extends Block_c implements SJOutbranch
{
	private List targets; // Duplicated from SJBasicOperation_c.
	
	private SJLabel lab;

	private boolean isDependentlyTyped;
	
	public SJOutbranch_c(Position pos, List statements, SJLabel lab, List<Receiver> targets)
	{
		this(pos, statements, lab, targets, false);
	}

	public SJOutbranch_c(Position pos, List statements, SJLabel lab, List<Receiver> targets, boolean isDependentlyTyped)
	{
		super(pos, statements);

		this.lab = lab;
		this.targets = targets;
		this.isDependentlyTyped = isDependentlyTyped;
	}
	
	public SJLabel label()
	{
		return lab;
	}

	public List targets()
	{
		return targets; 
	}
	
	public SJOutbranch targets(List targets)
	{
		this.targets = targets;
		
		return this;
	}

    public List<Receiver> ambiguousTargets() {
        return null;
    }

    public List<SJVariable> resolvedTargets() {
        return null;
    }

    public SJSessionOperation resolvedTargets(List<SJVariable> resolved) {
        return null;
    }
    // Should set entry point to the socket operation for flow graph building. This is common for all structural operations.
    
  public boolean isDependentlyTyped()
  {
  	return isDependentlyTyped;
  }    
}
