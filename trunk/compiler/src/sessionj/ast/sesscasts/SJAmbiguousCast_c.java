package sessionj.ast.sesscasts;

import polyglot.ast.*;
import polyglot.util.Position;

import sessionj.ast.typenodes.*;

public class SJAmbiguousCast_c extends SJSessionTypeCast_c implements SJAmbiguousCast
{
	public SJAmbiguousCast_c(Position pos, TypeNode castType, Expr expr, SJTypeNode tn)
	{
		super(pos, castType, expr, tn);
	}
}
