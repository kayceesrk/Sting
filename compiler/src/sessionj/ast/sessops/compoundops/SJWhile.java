package sessionj.ast.sessops.compoundops;

import polyglot.ast.*;

public interface SJWhile extends While, SJLoopOperation
{
	SJWhile cond(Expr cond);
	SJWhile body(Stmt body);
}
