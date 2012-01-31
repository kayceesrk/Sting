package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;

public interface SJInwhile extends SJWhile
{
	SJInwhile cond(Expr cond);
	SJInwhile body(Stmt body);
}
