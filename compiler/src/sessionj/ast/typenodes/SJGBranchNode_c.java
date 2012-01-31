//<By MQ> Added
package sessionj.ast.typenodes;

import java.util.*;

import polyglot.util.Position;

import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;
import sessionj.types.sesstypes.SJBranchType;
import sessionj.types.SJTypeSystem;

public class SJGBranchNode_c extends SJBranchNode_c implements SJGBranchNode
{
    public String guard;
    public SJGBranchNode_c(Position pos, String guard, List<SJBranchCaseNode> branchCases)
	{
		this(pos, branchCases, false);
		this.guard = guard;
	}
   
  public SJGBranchNode_c(Position pos, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped)
	{
		super(pos, branchCases, isDependentlyTyped);
	}    

	public SJGBranchNode branchCases(List<SJBranchCaseNode> branchCases)
	{
		return (SJGBranchNode) super.branchCases(branchCases); 
	}

    protected SJBranchType createType(SJTypeSystem sjts) {
        return sjts.SJGBranchType();
    }

    public String nodeToString()
	{
		String s = guard + ":{";

		for (Iterator<SJBranchCaseNode> i = branchCases().iterator(); i.hasNext(); )
		{
			SJBranchCaseNode branchCase = i.next();
			SJLabel lab = branchCase.label();
			SJTypeNode body = branchCase.body();

			s += lab + SJ_STRING_LABEL;
			s += body == null ? " " : body.toString();

			if (i.hasNext()) 
			{
				s += SJ_STRING_CASE_SEPARATOR + " ";
			}
		}

		return s + "}";
	}
}
