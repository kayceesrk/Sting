package sessionj.ast.typenodes;

import polyglot.frontend.Job;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;
import sessionj.types.SJTypeSystem;

public interface SJLoopNode extends SJTypeNode
{
	SJTypeNode body();
	SJLoopNode body(SJTypeNode body);
}
