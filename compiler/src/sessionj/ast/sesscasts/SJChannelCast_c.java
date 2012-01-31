package sessionj.ast.sesscasts;

import polyglot.ast.*;
import polyglot.util.Position;

import sessionj.ast.typenodes.*;

public class SJChannelCast_c extends SJSessionTypeCast_c implements SJChannelCast
{
	public SJChannelCast_c(Position pos, TypeNode castType, Expr expr, SJTypeNode tn)
	{
		super(pos, castType, expr, tn);
	}
}
