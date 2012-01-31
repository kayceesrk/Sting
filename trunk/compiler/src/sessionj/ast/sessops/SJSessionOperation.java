package sessionj.ast.sessops;

import java.util.List;

import sessionj.ast.SJTypeable;
import sessionj.ast.sessvars.SJVariable;
import polyglot.ast.Receiver;

public interface SJSessionOperation extends SJTypeable
{
	/**
     * First (ambiguous) Receivers and later SJSocketVariables.
     * Aliases the actual AST nodes.
     * Confusing because the contents of the list change as nodes are visited,
     * use the two new methods instead.
     */
    @Deprecated
    List targets();
    List<Receiver> ambiguousTargets();
    List<SJVariable> resolvedTargets();
    @Deprecated
	SJSessionOperation targets(List target);
    SJSessionOperation resolvedTargets(List<SJVariable> resolved);
}
