package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;

public interface SJOutwhile extends SJWhile
{
	SJOutwhile cond(Expr cond);
	SJOutwhile body(Stmt body);

    boolean isInterruptible();
}
