package sessionj.ast.sessops.compoundops;

import polyglot.ast.FieldDecl;
import polyglot.ast.Node;
import polyglot.ast.ClassMember;
import polyglot.qq.QQ;
import polyglot.types.SemanticException;
import polyglot.visit.ContextVisitor;
import sessionj.ast.SessionTypedNode;
import sessionj.ast.sessops.TraverseTypeBuildingContext;
import sessionj.types.sesstypes.SJSessionType;

import java.util.Collection;

public interface SJTypecase extends SJCompoundOperation, TraverseTypeBuildingContext, SessionTypedNode {
    SJCompoundOperation sessionTypeCheck(SJSessionType typeForNode) throws SemanticException; // TODO: factor out the session type checking interface.

    Node translate(QQ qq, ContextVisitor sjte, Collection<ClassMember> fieldsToAdd) throws SemanticException;
}
