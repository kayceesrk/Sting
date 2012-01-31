package sessionj.ast.typenodes;

import polyglot.frontend.Job;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;
import sessionj.types.SJTypeSystem;

public interface SJSetTypeNode extends SJTypeNode {
    boolean isSingleton();

    SJTypeNode get(int index);
}
