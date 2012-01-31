package sessionj.ast;

import polyglot.ast.Node;
import polyglot.visit.ContextVisitor;
import polyglot.types.SemanticException;
import sessionj.types.SJTypeSystem;
import sessionj.extension.SJExtFactory;

public interface SessionTypedNode {
    Node buildType(ContextVisitor cv, SJTypeSystem sjts, SJExtFactory sjef) throws SemanticException;
}
