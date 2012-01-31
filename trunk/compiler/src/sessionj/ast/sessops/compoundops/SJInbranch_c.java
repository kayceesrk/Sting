package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.visit.*;
import polyglot.util.*;

import java.util.*;

import sessionj.ast.sessops.basicops.SJInlabel;
import sessionj.ast.sessops.SJSessionOperation;
import sessionj.ast.sessvars.SJVariable;
import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;

/**
 * An "expression-less switch"
 */
public class SJInbranch_c extends Stmt_c implements SJInbranch
{
	private SJInlabel il;
	
	private List targets; // Duplicated from SJBasicOperation_c.
	
	protected List<SJInbranchCase> branchCases;

	private boolean isDependentlyTyped;
	
	public SJInbranch_c(Position pos, List<SJInbranchCase> branchCases, SJInlabel il)
	{
		this(pos, branchCases, il, false);
	}

	public SJInbranch_c(Position pos, List<SJInbranchCase> branchCases, SJInlabel il, boolean isDependentlyTyped)
	{
		super(pos);

		this.branchCases = TypedList.copyAndCheck(branchCases, SJInbranchCase.class, true); // Is this check necessary?
        targets = il.targets();
		this.il = il;
		
		this.isDependentlyTyped = isDependentlyTyped;
	}
	
	public List<SJInbranchCase> branchCases()
	{
		return Collections.unmodifiableList(branchCases); // Why unmodifiable?
	}

	public SJInbranch branchCases(List<SJInbranchCase> branchCases)
	{
		SJInbranch_c n = (SJInbranch_c) copy();

		n.branchCases = TypedList.copyAndCheck(branchCases, SJInbranchCase.class, true); // Generic mixed up with Polyglot.

		return n;
	}

	public List targets()
	{
		return targets; 
	}
	
	public SJInbranch targets(List targets)
	{
		this.targets = targets;
		
		return this;
	}	
	
	public SJInlabel inlabel()
	{
		return il;		
	}
	
	public SJInbranch inlabel(SJInlabel il)
	{
		this.il = il;
		
		return this;
	}
	
	public Node typeCheck(TypeChecker tc) throws SemanticException // Children checked by type checker before this node is visited (from leaveCall).
	{
		Collection<SJLabel> labels = new HashSet<SJLabel>();

		for (SJInbranchCase ibc : branchCases) // Move this to SJSessionTypeChecker to be uniform?
		{
			SJLabel lab = ibc.label();

			if (labels.contains(lab))
			{
				throw new SemanticException("[SJInbranch_c] Duplicate case label: " + lab + '.', ibc.position());
			}

			labels.add(lab);
		}
		
		return this;
	}

	public String toString()
	{
		return targetsToString() + '.' + SJ_KEYWORD_INBRANCH + "() { ... }";
	}
	
	// The following are adapted from Switch_c.
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) // This is largely redundant (except for debugging) due to later translation.
	{
		w.write(targetsToString() + '.' + SJ_KEYWORD_INBRANCH + "()"); // Should use the SJCompilerUtils operation.
		w.write(" {");
		w.allowBreak(4, " ");
		w.begin(0);

        for (SJInbranchCase s : branchCases) {
            w.allowBreak(4, " ");
            print(s, w, tr);
        }

		w.end();
		w.allowBreak(0, " ");
		w.write("}");
	}
	
	protected SJInbranch_c reconstruct(SJInlabel il, List<SJInbranchCase> branchCases)
	{
        this.il = il;
		
		if (!CollectionUtil.equals(branchCases, this.branchCases))
		{
			SJInbranch_c n = (SJInbranch_c) copy();

			n.branchCases = TypedList.copyAndCheck(branchCases, SJInbranchCase.class, true);

			return n;
		}

		return this;
	}

	public Context enterScope(Context c)
	{
		return c.pushBlock();
	}

	public Node visitChildren(NodeVisitor v)
	{
		SJInlabel il = (SJInlabel) visitChild(firstChild(), v);
		List<SJInbranchCase> branchCases = visitList(branchCases(), v);
		
		return reconstruct(il, branchCases);
	}
	
	
	public Term firstChild()
	{
		return inlabel();
	}

	public List acceptCFG(CFGBuilder v, List succs) 
	{
		List<Term> cases = new LinkedList<Term>();
		List<Integer> entry = new LinkedList<Integer>(); 
		
		for (SJInbranchCase ibc : branchCases)
		{
			cases.add(ibc);
			entry.add(ENTRY);
		}

		cases.add(this);
    entry.add(EXIT);
		
		v.visitCFG(inlabel(), FlowGraph.EDGE_KEY_OTHER, cases, entry); // entry...
		
		v.push(this).visitCFGList(branchCases, this, EXIT); // ...and exit points?
		
		return succs;
	}
	
	private String targetsToString()
	{
		String m = targets().toString();
		
		return '<' + m.substring(1, m.length() - 1) + '>';
	}
    // end duplicated code from Switch_c

    public List<Receiver> ambiguousTargets() {
        return null;
    }

    public List<SJVariable> resolvedTargets() {
        return null;
    }

    public SJSessionOperation resolvedTargets(List<SJVariable> resolved) {
        return null;
    }
    
  public boolean isDependentlyTyped()
  {
  	return isDependentlyTyped;
  }
}
