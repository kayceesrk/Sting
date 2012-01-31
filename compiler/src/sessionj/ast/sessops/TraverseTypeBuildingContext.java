package sessionj.ast.sessops;

import polyglot.types.SemanticException;
import sessionj.types.contexts.SJContextElement;
import sessionj.types.contexts.SJContextInterface;

// Rename: this is a base class for AST nodes, not a context itself.
public interface TraverseTypeBuildingContext {
    void enterSJContext(SJContextInterface sjcontext) throws SemanticException;
    SJContextElement leaveSJContext(SJContextInterface sjcontext) throws SemanticException;
}
