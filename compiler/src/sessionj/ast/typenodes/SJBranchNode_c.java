package sessionj.ast.typenodes;

import java.util.List;
import java.util.LinkedList;

import polyglot.util.Position;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;

import sessionj.types.SJTypeSystem;
import sessionj.types.sesstypes.SJBranchType;
import sessionj.util.SJCompilerUtils;

abstract public class SJBranchNode_c extends SJTypeNode_c implements SJBranchNode
{
	private List<SJBranchCaseNode> branchCases;

	private boolean isDependentlyTyped;

	public SJBranchNode_c(Position pos, List<SJBranchCaseNode> branchCases) // Probably redundant now.
	{
		this(pos, branchCases, false);
	}
	
	public SJBranchNode_c(Position pos, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped)
	{
		super(pos);

		this.branchCases = branchCases;
		this.isDependentlyTyped = isDependentlyTyped;
	}

        public List<SJBranchCaseNode> branchCases()  //<By MQ> changed from protected
	{
		return branchCases;
	}

	protected SJBranchNode branchCases(List<SJBranchCaseNode> branchCases)
	{
		this.branchCases = branchCases;

		return this;
	}

    public SJTypeNode disambiguateSJTypeNode(ContextVisitor cv, SJTypeSystem sjts) throws SemanticException 
    {
        SJBranchType bt = createType(sjts);

        List<SJBranchCaseNode> branchCases = new LinkedList<SJBranchCaseNode>();

        for (SJBranchCaseNode bcn : branchCases()) 
        {
            bcn = (SJBranchCaseNode) SJCompilerUtils.disambiguateSJTypeNode(cv, bcn);

            branchCases.add(bcn);

            bt = bt.branchCase(bcn.label(), bcn.type());
        }

        return (SJTypeNode) branchCases(branchCases).type(bt);
    }

    protected abstract SJBranchType createType(SJTypeSystem sjts);

  public boolean isDependentlyTyped()
  {
  	return isDependentlyTyped;
  }
}
