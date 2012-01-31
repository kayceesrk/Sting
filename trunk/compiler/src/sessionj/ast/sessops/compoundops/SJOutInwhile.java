package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;

import java.util.List;

/**
 * 
 * @author Raymond
 *
 * Probably not useful (or actually misleading) to make this an actual SJOutwhile. For this, would probably need to separate the inwhile target from the outwhile targets, and so would need special treatment at more stages, e.g. SJVariableParser.
 *
 * Currently implicitly using the first target as the inwhile target. 
 *
 * FIXME: outinwhile, being a single node, only currently (explicitly) typed for the outwhile part. Maybe should be made an SJOutwhile with separate inwhile part including the target and the type.  
 *
 */
public interface SJOutInwhile extends SJWhile
{
	SJOutInwhile cond(Expr cond);
	SJOutInwhile body(Stmt body);
    boolean hasCondition();
    List<?> outsyncTargets();
    List<?> insyncSources();
}
