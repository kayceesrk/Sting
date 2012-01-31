package sessionj.ast.typenodes;

import sessionj.util.SJLabel;
import polyglot.frontend.Job;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;

public interface SJBranchCaseNode extends SJTypeNode
{
	SJLabel label();

	SJTypeNode body();
	
	boolean isDependentlyTyped();
}
