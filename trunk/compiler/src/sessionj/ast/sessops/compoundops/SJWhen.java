package sessionj.ast.sessops.compoundops;

import sessionj.ast.sessops.TraverseTypeBuildingContext;
import sessionj.ast.SessionTypedNode;
import sessionj.types.sesstypes.SJSetType;
import sessionj.types.sesstypes.SJSessionType;
import polyglot.ast.Block;
import polyglot.types.SemanticException;

public interface SJWhen extends Block, TraverseTypeBuildingContext, SessionTypedNode {
    SJSessionType selectMatching(SJSetType set) throws SemanticException;

    SJSessionType type();
}
