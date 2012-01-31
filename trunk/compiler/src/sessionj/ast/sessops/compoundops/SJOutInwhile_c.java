package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;
import polyglot.util.Position;
import polyglot.parse.Name;

import java.util.LinkedList;
import java.util.List;

public class SJOutInwhile_c extends SJWhile_c implements SJOutInwhile
{
    private int sourcesStart;
    private boolean hasCondition;

    public SJOutInwhile_c(Position pos, Expr cond, Stmt body, List targets, List sources)
	{
		super(pos, ensureNotNull(pos, cond), body, addAllToList(targets, sources));
        hasCondition = cond != null;
        // The super() call has to be passed a non-null condition, but we need to keep track
        // of whether there really was one or if we put a dummy one in.
        sourcesStart = targets.size();
        // This weird hack is because the SJVariableParser visitor comes over and changes the contents
        // of the targets() list - disambiguating from AmbReceivers to Exprs. So we can't really
        // use generics either, it makes things even more confusing...
    }

    private static Expr ensureNotNull(Position pos, Expr cond) {
        // false, not true, so that we don't mess with reachability analysis
        // (which is performed by Polyglot before we insert the real condition
        // in SJCompoundOperationTranslator).
        if (cond == null) return new BooleanLit_c(pos, false);
        return cond;
    }

    private static List addAllToList(List targets, List sources) {
        List l = new LinkedList(targets);
        l.addAll(sources);
        return l;
    }

    public List<?> outsyncTargets() {
        return targets().subList(0, sourcesStart);
    }

    public List insyncSources() {
        return targets().subList(sourcesStart, targets().size());
    }

    public SJOutInwhile cond(Expr cond)
	{
		return (SJOutInwhile) super.cond(cond);
	}

	public SJOutInwhile body(Stmt body)
	{
		return (SJOutInwhile) super.body(body);
	}

    public boolean hasCondition() {
        return hasCondition;
    }
}
