package sessionj.ast.sesscasts;

import polyglot.ast.Cast;

import sessionj.ast.SJTypeable;
import sessionj.ast.typenodes.*;

public interface SJSessionTypeCast extends Cast, SJTypeable
{
	public SJTypeNode sessionType();
	public SJSessionTypeCast sessionType(SJTypeNode tn);
}
