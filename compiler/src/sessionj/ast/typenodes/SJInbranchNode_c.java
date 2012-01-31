package sessionj.ast.typenodes;

import java.util.*;

import polyglot.util.Position;

import sessionj.util.SJLabel;

import static sessionj.SJConstants.*;
import sessionj.types.sesstypes.SJBranchType;
import sessionj.types.SJTypeSystem;
import polyglot.ast.Id; //<By MQ>

public class SJInbranchNode_c extends SJBranchNode_c implements SJInbranchNode
{
        //<By MQ>
        private String target;
        public SJInbranchNode_c(Position pos, String target, List<SJBranchCaseNode> branchCases) //<By MQ>
	{
	        this(pos, target, branchCases, false);
	}
   
        public SJInbranchNode_c(Position pos, String target, List<SJBranchCaseNode> branchCases, boolean isDependentlyTyped)
	{
		super(pos, branchCases, isDependentlyTyped);
	        this.target = target;
	}    
        public String target()
        {
	    return target;
        }
        //</By MQ>
	public SJInbranchNode branchCases(List<SJBranchCaseNode> branchCases)
	{
		return (SJInbranchNode) super.branchCases(branchCases); 
	}

    protected SJBranchType createType(SJTypeSystem sjts) {
        return sjts.SJInbranchType(target);
    }

    public String nodeToString()
	{
	        String s = target + ":" + SJ_STRING_INBRANCH_OPEN; //<By MQ>

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

		return s + SJ_STRING_INBRANCH_CLOSE;
	}
}
