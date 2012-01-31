package sessionj.ast.sesscasts;

import polyglot.ast.*;
import polyglot.util.Position;

import sessionj.ast.typenodes.*;

public class SJSessionCast_c extends SJSessionTypeCast_c implements SJSessionCast
{
	public SJSessionCast_c(Position pos, TypeNode castType, Expr expr, SJTypeNode tn)
	{
		super(pos, castType, expr, tn);
	}
}
