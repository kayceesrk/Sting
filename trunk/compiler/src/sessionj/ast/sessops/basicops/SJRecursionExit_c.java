package sessionj.ast.sessops.basicops;

import polyglot.util.Position;
import polyglot.ast.Expr;
import sessionj.ast.SJNodeFactory;

import java.util.List;
import java.util.LinkedList;

public class SJRecursionExit_c extends SJBasicOperation_c implements SJRecursionExit  
{	
	public SJRecursionExit_c(Position pos, SJNodeFactory nf, String name, List targets)
	{
		super(pos, nf, name, new LinkedList<Expr>(), targets);
	}
}
