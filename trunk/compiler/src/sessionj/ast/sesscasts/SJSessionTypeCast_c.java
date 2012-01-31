package sessionj.ast.sesscasts;

import polyglot.ast.*;
import polyglot.util.Position;

import sessionj.ast.typenodes.*;

abstract public class SJSessionTypeCast_c extends Cast_c implements SJSessionTypeCast
{
	private SJTypeNode tn;
	
	public SJSessionTypeCast_c(Position pos, TypeNode castType, Expr expr, SJTypeNode tn)
	{
		super(pos, castType, expr);
		
		this.tn = tn;
	}
	
	public SJTypeNode sessionType()
	{
		return tn;
	}
	
	public SJSessionTypeCast sessionType(SJTypeNode tn)
	{
		this.tn = tn;
		
		return this;
	}
}
